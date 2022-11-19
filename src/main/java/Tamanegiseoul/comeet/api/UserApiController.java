package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.StatusCode;
import Tamanegiseoul.comeet.dto.user.request.JoinUserRequest;
import Tamanegiseoul.comeet.dto.user.request.RemoveUserRequest;
import Tamanegiseoul.comeet.dto.user.request.SearchUserRequest;
import Tamanegiseoul.comeet.dto.user.request.UpdateUserRequest;
import Tamanegiseoul.comeet.dto.user.response.JoinUserResponse;
import Tamanegiseoul.comeet.dto.user.response.SearchUserResponse;
import Tamanegiseoul.comeet.dto.user.response.UpdateUserResponse;
import Tamanegiseoul.comeet.service.CommentService;
import Tamanegiseoul.comeet.service.PostService;
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

            userService.updatePreferStack(newUser.getId(), request.getStacks());
            log.info("%s's preferred tech stack has been registered", newUser.getNickname());

            List<TechStack> preferredStacks = userService.findPreferredStacks(newUser.getId());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.CREATED_USER,
                    JoinUserResponse.builder()
                            .userId(newUser.getId())
                            .email(newUser.getEmail())
                            .nickname(newUser.getNickname())
                            .preferredStacks(preferredStacks)
                            .build()
                    );
        } catch (DuplicateResourceException e) {
            return ApiResponse.of(HttpStatus.FORBIDDEN, ResponseMessage.DUPLICATE_RES, e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse> removeUser(@RequestBody @Valid RemoveUserRequest request) {
        try {
            userService.removeUser(request.getUserId());
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.DELETE_USER, request.getUserId());
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, request.getUserId());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchUser(@RequestBody @Valid SearchUserRequest request) {
        try {
            Users findUser = userService.findUserById(request.getUserId());

            List<TechStack> preferredStacks = userService.findPreferredStacks(findUser.getId());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_USER, SearchUserResponse.builder()
                    .id(findUser.getId())
                    .email(findUser.getEmail())
                    .nickname(findUser.getNickname())
                    .stacks(preferredStacks)
                    .build());
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, e.getMessage());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody @Valid UpdateUserRequest request) {
        try {
            Users updatedUser = userService.updateUser(request);

            List<TechStack> preferredStacks = userService.findPreferredStacks(updatedUser.getId());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.UPDATE_USER,
                    UpdateUserResponse.builder()
                            .userId(request.getUserId())
                            .nickname(updatedUser.getNickname())
                            .email(updatedUser.getEmail())
                            .preferredStacks(preferredStacks)
                            .build()
                    );
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateResourceException e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.DUPLICATE_RES, e.getMessage());
        }
    }


}
