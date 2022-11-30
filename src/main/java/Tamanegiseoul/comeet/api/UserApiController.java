package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.StatusCode;
import Tamanegiseoul.comeet.dto.user.request.*;
import Tamanegiseoul.comeet.dto.user.response.*;
import Tamanegiseoul.comeet.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserApiController {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final ImageDataService imageDataService;
    private final StackRelationService stackRelationService;

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse> validate(@RequestBody @Valid ValidateUserRequest request) {
        try {
            userService.validateUserEmail(request.getEmail());
            userService.validateUserEmail(request.getNickname());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.RESOURCE_AVAILABLE, request);
        } catch (DuplicateResourceException e) {
            return ApiResponse.of(HttpStatus.FORBIDDEN, ResponseMessage.DUPLICATE_RES, e.getMessage());
        }
    }

    @PostMapping("/join")
    //public ResponseEntity<ApiResponse> joinNewUser(@RequestBody @Valid JoinUserRequest request, @RequestParam("image")MultipartFile file) {
    public ResponseEntity<ApiResponse> joinNewUser(@RequestPart("request") @Valid JoinUserRequest request, @Nullable @RequestPart("image")MultipartFile file) {
        Users newUser = Users.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(request.getPassword())
                .build();
        try {
            userService.registerUser(newUser);
            log.info("[%s] %s has been registered.", newUser.getNickname(), newUser.getEmail());

            userService.updatePreferStack(newUser.getUserId(), request.getPreferStacks());
            log.info("%s's preferred tech stack has been registered", newUser.getNickname());

            ImageDto imageDto = null;

            if(file != null) {
                log.warn("[UserApiController:joinNewUser]file is present");
                imageDto = imageDataService.uploadImage(newUser, file);
            } else {
                log.warn("[UserApiController:joinNewUser]file is empty");
            }

            List<TechStack> preferredStacks = userService.findPreferredStacks(newUser.getUserId());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.CREATED_USER,
                    JoinUserResponse.builder()
                            .userId(newUser.getUserId())
                            .email(newUser.getEmail())
                            .nickname(newUser.getNickname())
                            .preferStacks(preferredStacks)
                            .createdTime(newUser.getCreatedTime())
                            .modifiedTime(newUser.getModifiedTime())
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

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse> removeUser(@RequestBody @Valid RemoveUserRequest request) {
        try {
            Users findUser = userService.findUserById(request.getUserId());
            RemoveUserResponse response = RemoveUserResponse.builder()
                    .userId(findUser.getUserId())
                    .nickname(findUser.getNickname())
                    .build();

            int removedUserId = userService.removeUser(request.getUserId());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.DELETE_USER, response);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, request);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchUser(@RequestBody @Valid SearchUserRequest request) {
        try {
            Users findUser = userService.findUserById(request.getUserId());

            ImageDto findImage = imageDataService.findImageByUserId(findUser.getUserId());

            List<TechStack> preferredStacks = userService.findPreferredStacks(findUser.getUserId());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_USER, SearchUserResponse.builder()
                            .userId(findUser.getUserId())
                            .email(findUser.getEmail())
                            .nickname(findUser.getNickname())
                            .preferStacks(preferredStacks)
                            .createdTime(findUser.getCreatedTime())
                            .modifiedTime(findUser.getModifiedTime())
                            .profileImage(findImage)
                            .build());
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, e.getMessage());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestPart("request") @Valid UpdateUserRequest request, @Nullable @RequestPart("image")MultipartFile file) {
        try {
            Users updatedUser = userService.updateUser(request);
            ImageDto imageDto = null;
            if(file != null) {
                log.warn("[UserApiController:updateUser] file is present");
                ImageDto findImage = imageDataService.findImageByUserId(updatedUser.getUserId());
                if(findImage != null) {
                    log.warn("[UserApiController:updateUser] updated registered image");
                    imageDto = imageDataService.updateImage(updatedUser, file);
                } else {
                    log.warn("[UserApiController:updateUser] upload new profile image");
                    imageDto = imageDataService.uploadImage(updatedUser, file);
                }

            } else {
                log.warn("[UserApiController:updateUser] file is empty");
            }

            List<TechStack> preferredStacks = userService.findPreferredStacks(updatedUser.getUserId());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.UPDATE_USER,
                    UpdateUserResponse.builder()
                            .userId(request.getUserId())
                            .nickname(updatedUser.getNickname())
                            .email(updatedUser.getEmail())
                            .preferredStacks(preferredStacks)
                            .createdTime(updatedUser.getCreatedTime())
                            .modifiedTime(updatedUser.getModifiedTime())
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
