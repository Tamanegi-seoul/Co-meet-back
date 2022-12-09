package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.User;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.auth.request.JoinRequest;
import Tamanegiseoul.comeet.dto.auth.request.SigninRequest;
import Tamanegiseoul.comeet.dto.auth.response.SigninResponse;
import Tamanegiseoul.comeet.dto.user.response.ImageDto;
import Tamanegiseoul.comeet.dto.auth.response.JoinResponse;
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


    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> authenticate(@RequestBody @Valid SigninRequest request) {
        User user = null; //= userService.getByCredentials(request.getEmail(), request.getPassword(), passwordEncoder);

        if(user != null) {
//            final String token = tokenProvider.create(user);
//            final SigninResponse response = SigninResponse.builder()
//                    .token(token)
//                    .userId(user.getUserId())
//                    .email(user.getEmail())
//                    .nickname(user.getNickname())
//                    .build();
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.LOGIN_SUCCESS, null);
        } else {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);
        }
    }

}
