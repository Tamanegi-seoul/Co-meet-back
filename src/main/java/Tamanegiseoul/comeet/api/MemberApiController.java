package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.member.request.JoinMemberRequest;
import Tamanegiseoul.comeet.dto.member.response.JoinMemberResponse;
import Tamanegiseoul.comeet.dto.member.request.*;
import Tamanegiseoul.comeet.dto.member.response.*;
import Tamanegiseoul.comeet.security.JwtProvider;
import Tamanegiseoul.comeet.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/member")
@Tag(name = "Member API", description = "회원 관련 CRUD 기능 제공")
public class MemberApiController {
    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;
    private final ImageDataService imageDataService;
    private final StackRelationService stackRelationService;

    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @GetMapping("/profile")
    @Operation(summary = "닉네임/이메일 중복 검증", description = "회원가입에 대한 이메일/닉네임 가용여부 검증")
    public ResponseEntity<ApiResponse> validate(@RequestParam("nickname") String nickname, @RequestParam("email") String email ) {
        try {
            memberService.validateMemberEmail(email);
            memberService.validateMemberEmail(nickname);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.RESOURCE_AVAILABLE);
        } catch (DuplicateResourceException e) {
            return ApiResponse.of(HttpStatus.FORBIDDEN, ResponseMessage.DUPLICATE_RES, e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "신규 회원가입", description = "새로운 회원 등록")
    public ResponseEntity<ApiResponse> joinNewMember(@RequestPart("request") @Valid JoinMemberRequest request, @Nullable @RequestPart("image") MultipartFile image) {
        try {
            JoinMemberResponse responseDto = memberService.registerMember(request, image);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.CREATED_USER, responseDto);

        } catch (DuplicateResourceException e) {
            return ApiResponse.of(HttpStatus.FORBIDDEN, ResponseMessage.DUPLICATE_RES, e.getMessage());
        } catch (IOException e) {
            log.warn(e.getMessage());
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.FAIL_FILE_UPLOAD, e.getMessage());
        }
    }


    @DeleteMapping
    @Operation(summary = "회원 탈퇴", description = "등록된 회원 탈퇴")
    public ResponseEntity<ApiResponse> removeMember(@RequestParam("memberId") @Valid Long memberId) {
        try {

            Member findMember = memberService.findMemberById(memberId);
            RemoveMemberResponse response = RemoveMemberResponse.builder()
                    .memberId(findMember.getMemberId())
                    .nickname(findMember.getNickname())
                    .build();

            Long removedMemberId = memberService.removeMember(memberId);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.DELETE_USER, response);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, null);
        }
    }

    @GetMapping
    @Operation(summary = "회원 조회", description = "등록된 회원정보 조회")
    public ResponseEntity<ApiResponse> searchMember(@RequestParam("memberId") Long memberId) {
        try {

            Member findMember = memberService.findMemberById(memberId);

            ImageDto findImage = imageDataService.findImageByMemberId(findMember.getMemberId());

            List<TechStack> preferredStacks = memberService.findPreferredStacks(findMember.getMemberId());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_USER, SearchMemberResponse.builder()
                            .memberId(findMember.getMemberId())
                            .email(findMember.getEmail())
                            .nickname(findMember.getNickname())
                            .preferStacks(preferredStacks)
                            .createdTime(findMember.getCreatedTime())
                            .modifiedTime(findMember.getModifiedTime())
                            .profileImage(findImage)
                            .build());
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, e.getMessage());
        }
    }


    @PatchMapping
    @Operation(summary = "회원 수정", description = "등록된 회원 정보 수정")
    public ResponseEntity<ApiResponse> updateMember(@RequestPart("request") @Valid UpdateMemberRequest request, @Nullable @RequestPart("image")MultipartFile file) {
        try {
            Member updatedMember = memberService.updateMember(request);
            ImageDto imageDto = null;
            if(file != null) {
                log.warn("[MemberApiController:updateMember] file is present");
                ImageDto findImage = imageDataService.findImageByMemberId(updatedMember.getMemberId());
                if(findImage != null) {
                    log.warn("[MemberApiController:updateMember] updated registered image");
                    imageDto = imageDataService.updateImage(updatedMember, file);
                } else {
                    log.warn("[MemberApiController:updateMember] upload new profile image");
                    imageDto = imageDataService.uploadImage(updatedMember, file);
                }

            } else {
                log.warn("[MemberApiController:updateMember] file is empty");
                imageDataService.removeImage(updatedMember);
            }

            List<TechStack> preferredStacks = memberService.findPreferredStacks(updatedMember.getMemberId());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.UPDATE_USER,
                    UpdateMemberResponse.builder()
                            .memberId(request.getMemberId())
                            .nickname(updatedMember.getNickname())
                            .email(updatedMember.getEmail())
                            .preferStacks(preferredStacks)
                            .createdTime(updatedMember.getCreatedTime())
                            .modifiedTime(updatedMember.getModifiedTime())
                            .profileImage(imageDto)
                            .build()
            );
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, e.getMessage());
        } catch (DuplicateResourceException e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.DUPLICATE_RES, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.FAIL_FILE_UPLOAD, e.getMessage());
        }
    }

}
