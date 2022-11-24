package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.StatusCode;
import Tamanegiseoul.comeet.dto.user.request.*;
import Tamanegiseoul.comeet.dto.user.response.JoinUserResponse;
import Tamanegiseoul.comeet.dto.user.response.RemoveUserResponse;
import Tamanegiseoul.comeet.dto.user.response.SearchUserResponse;
import Tamanegiseoul.comeet.dto.user.response.UpdateUserResponse;
import Tamanegiseoul.comeet.service.CommentService;
import Tamanegiseoul.comeet.service.PostService;
import Tamanegiseoul.comeet.service.StackRelationService;
import Tamanegiseoul.comeet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<ApiResponse> joinNewUser(@RequestBody @Valid JoinUserRequest request) {
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

            List<TechStack> preferredStacks = userService.findPreferredStacks(newUser.getUserId());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.CREATED_USER,
                    JoinUserResponse.builder()
                            .userId(newUser.getUserId())
                            .email(newUser.getEmail())
                            .nickname(newUser.getNickname())
                            .preferStacks(preferredStacks)
                            .build()
                    );
        } catch (DuplicateResourceException e) {
            return ApiResponse.of(HttpStatus.FORBIDDEN, ResponseMessage.DUPLICATE_RES, e.getMessage());
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

            List<TechStack> preferredStacks = userService.findPreferredStacks(findUser.getUserId());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_USER, SearchUserResponse.builder()
                    .userId(findUser.getUserId())
                    .email(findUser.getEmail())
                    .nickname(findUser.getNickname())
                    .preferStacks(preferredStacks)
                    .build());
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, e.getMessage());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody @Valid UpdateUserRequest request) {
        try {
            Users updatedUser = userService.updateUser(request);



            List<TechStack> preferredStacks = userService.findPreferredStacks(updatedUser.getUserId());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.UPDATE_USER,
                    UpdateUserResponse.builder()
                            .userId(request.getUserId())
                            .nickname(updatedUser.getNickname())
                            .email(updatedUser.getEmail())
                            .preferredStacks(preferredStacks)
                            .build()
                    );
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, e.getMessage());
        } catch (DuplicateResourceException e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.DUPLICATE_RES, e.getMessage());
        }
    }


}
