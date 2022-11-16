package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.StatusCode;
import Tamanegiseoul.comeet.dto.user.request.JoinUserRequest;
import Tamanegiseoul.comeet.dto.user.response.JoinUserResponse;
import Tamanegiseoul.comeet.service.CommentService;
import Tamanegiseoul.comeet.service.PostService;
import Tamanegiseoul.comeet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.JOINED_NEW_USER,
                    JoinUserResponse.builder()
                            .id(newUser.getId())
                            .email(newUser.getEmail())
                            .nickname(newUser.getNickname())
                            .preferredStacks(preferredStacks)
                            .build()
                    );
        } catch (DuplicateResourceException e) {
            return ApiResponse.of(HttpStatus.FORBIDDEN, ResponseMessage.DUPLICATE_RES, e.getMessage());
        }
    }
}
