package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Role;
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
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
//import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static Tamanegiseoul.comeet.dto.StatusCode.FORBIDDEN;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
public class MemberApiController {
    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;
    private final ImageDataService imageDataService;
    private final StackRelationService stackRelationService;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/member/validate")
    //@ApiOperation(value="검증", notes="포스트에 대한 덧글 작성")
    public ResponseEntity<ApiResponse> validate(@RequestParam("nickname") String nickname, @RequestParam("email") String email ) {
        try {
            memberService.validateMemberEmail(email);
            memberService.validateMemberEmail(nickname);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.RESOURCE_AVAILABLE);
        } catch (DuplicateResourceException e) {
            return ApiResponse.of(HttpStatus.FORBIDDEN, ResponseMessage.DUPLICATE_RES, e.getMessage());
        }
    }

    @PostMapping("/member/join")
    //public ResponseEntity<ApiResponse> joinNewMember(@RequestBody @Valid JoinUserRequest request, @RequestParam("image")MultipartFile file) {
    public ResponseEntity<ApiResponse> joinNewMember(@RequestPart("request") @Valid JoinMemberRequest request, @Nullable @RequestPart("image") MultipartFile file) {
        log.error(request.toString());
        log.error("join request's password {}", request.getPassword());
        Member newMember = Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(request.getPassword())
                .build();
        try {
            memberService.registerMember(newMember);
            log.info("[%s] %s has been registered.", newMember.getNickname(), newMember.getEmail());

            memberService.addRoleToMember(newMember.getEmail(), "ROLE_USER");

            memberService.updatePreferStack(newMember.getMemberId(), request.getPreferStacks());
            log.info("%s's preferred tech stack has been registered", newMember.getNickname());

            ImageDto imageDto = null;

            if(file != null) {
                log.warn("[MemberApiController:joinNewUser]file is present");
                imageDto = imageDataService.uploadImage(newMember, file);
            } else {
                log.warn("[MemberApiController:joinNewUser]file is empty");
            }

            List<TechStack> preferredStacks = memberService.findPreferredStacks(newMember.getMemberId());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.CREATED_USER,
                    JoinMemberResponse.builder()
                            .memberId(newMember.getMemberId())
                            .email(newMember.getEmail())
                            .nickname(newMember.getNickname())
                            .preferStacks(preferredStacks)
                            .createdTime(newMember.getCreatedTime())
                            .modifiedTime(newMember.getModifiedTime())
                            .profileImage(imageDto)
                            .build()
            );

        } catch (DuplicateResourceException e) {
            return ApiResponse.of(HttpStatus.FORBIDDEN, ResponseMessage.DUPLICATE_RES, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.FAIL_FILE_UPLOAD, e.getMessage());
        }
    }

    @PostMapping("/role/save")
    public ResponseEntity<ApiResponse> saveRole(@RequestBody @Valid Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        memberService.saveRole(role);
        //return ResponseEntity.created(uri).body(role);

        return null;
    }

    @PostMapping("/role/addToUser")
    public ResponseEntity<ApiResponse> setUserRole(@RequestBody @Valid Member member, @RequestBody @Valid Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());

        memberService.addRoleToMember(member.getNickname(), role.getRoleName());
        //return ResponseEntity.created(uri).body(role);

        return null;
    }



    @DeleteMapping("/member/remove")
    public ResponseEntity<ApiResponse> removeMember(@RequestBody @Valid RemoveMemberRequest request) {
        try {
            log.error("[MemberApiController:removeUser]method executed");
            log.error("[MemberApiController:removeUser]{}", request.toString());
            Long memberId = request.getMemberId();
            Member findMember = memberService.findMemberById(memberId);
            RemoveMemberResponse response = RemoveMemberResponse.builder()
                    .memberId(findMember.getMemberId())
                    .nickname(findMember.getNickname())
                    .build();

            int removedMemberId = memberService.removeMember(memberId);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.DELETE_USER, response);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, null);
        }
    }

    @GetMapping("/member/search")
    public ResponseEntity<ApiResponse> searchMember(@RequestParam("member_id") Long memberId) {
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


    @PatchMapping("/member/update")
    public ResponseEntity<ApiResponse> updateMember(@RequestHeader(AUTHORIZATION) String header, @RequestPart("request") @Valid UpdateMemberRequest request, @Nullable @RequestPart("image")MultipartFile file) {
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
            }

            List<TechStack> preferredStacks = memberService.findPreferredStacks(updatedMember.getMemberId());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.UPDATE_USER,
                    UpdateMemberResponse.builder()
                            .memberId(request.getMemberId())
                            .nickname(updatedMember.getNickname())
                            .email(updatedMember.getEmail())
                            .preferredStacks(preferredStacks)
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

    @GetMapping("/member/members")
    public ResponseEntity<List<Member>> getMembers() {
        return ResponseEntity.ok().body(memberService.findAll());
    }


    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("[MemberApiController:refreshToken]method executed");
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            log.info("[MemberApiController:refreshToken]authorizationHeader is valid");
            try {
                log.info("[MemberApiController:refreshToken]try refresh token");
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String memberEmail = decodedJWT.getSubject();
                Member member = memberService.findMemberByEmail(memberEmail);

                String accessToken = JWT.create()
                        .withSubject(member.getEmail()) // get email (security's username)
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000 )) // 10min
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("nickname", member.getNickname())
                        .withClaim("member_id", member.getMemberId())
                        .withClaim("roles", member.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()))
                        .sign(algorithm);

                /*
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
                 */
                Cookie accessCookie = new Cookie("access_token", accessToken);
                accessCookie.setMaxAge(7 * 86400);
                accessCookie.setComment("access_token");
                //accessCookie.setHttpOnly(true);
                accessCookie.setPath(request.getContextPath());
                Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
                refreshCookie.setMaxAge(14 * 86400);
                refreshCookie.setComment("refresh_token");
                //refreshCookie.setHttpOnly(true);
                refreshCookie.setPath(request.getContextPath());

                response.addCookie(accessCookie);
                response.addCookie(refreshCookie);


                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("status_code", String.valueOf(HttpStatus.OK));
                responseBody.put("response_message", ResponseMessage.REFRESH_TOKEN);
                responseBody.put("access_token", accessToken);
                responseBody.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), responseBody);


            }  catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN);
                //response.sendError(FORBIDDEN);
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }

    }


}
