package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.auth.request.JoinRequest;
import Tamanegiseoul.comeet.dto.auth.request.SigninRequest;
import Tamanegiseoul.comeet.dto.auth.response.SigninResponse;
import Tamanegiseoul.comeet.dto.user.response.ImageDto;
import Tamanegiseoul.comeet.dto.auth.response.JoinResponse;
import Tamanegiseoul.comeet.security.TokenProvider;
import Tamanegiseoul.comeet.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthApiController {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final ImageDataService imageDataService;
    private final StackRelationService stackRelationService;
    private final TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/join")
    //public ResponseEntity<ApiResponse> joinNewUser(@RequestBody @Valid JoinUserRequest request, @RequestParam("image")MultipartFile file) {
    public ResponseEntity<ApiResponse> joinNewUser(@RequestPart("request") @Valid JoinRequest request, @Nullable @RequestPart("image") MultipartFile file) {
        Users newUser = Users.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(passwordEncoder.encode(request.getPassword()))
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
                    JoinResponse.builder()
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

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> authenticate(@RequestBody @Valid SigninRequest request) {
        Users user = userService.getByCredentials(request.getEmail(), request.getPassword(), passwordEncoder);

        if(user != null) {
            final String token = tokenProvider.create(user);
            final SigninResponse response = SigninResponse.builder()
                    .token(token)
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .build();
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.LOGIN_SUCCESS, response);
        } else {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);
        }
    }

}
