package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.Role;
import Tamanegiseoul.comeet.domain.User;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.user.request.JoinUserRequest;
import Tamanegiseoul.comeet.dto.user.response.JoinUserResponse;
import Tamanegiseoul.comeet.dto.user.request.*;
import Tamanegiseoul.comeet.dto.user.response.*;
import Tamanegiseoul.comeet.security.JwtTokenProvider;
import Tamanegiseoul.comeet.service.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
public class UserApiController {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final ImageDataService imageDataService;
    private final StackRelationService stackRelationService;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/user/validate")
    public ResponseEntity<ApiResponse> validate(@RequestBody @Valid ValidateUserRequest request) {
        try {
            userService.validateUserEmail(request.getEmail());
            userService.validateUserEmail(request.getNickname());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.RESOURCE_AVAILABLE, request);
        } catch (DuplicateResourceException e) {
            return ApiResponse.of(HttpStatus.FORBIDDEN, ResponseMessage.DUPLICATE_RES, e.getMessage());
        }
    }

    @PostMapping("/user/join")
    //public ResponseEntity<ApiResponse> joinNewUser(@RequestBody @Valid JoinUserRequest request, @RequestParam("image")MultipartFile file) {
    public ResponseEntity<ApiResponse> joinNewUser(@RequestPart("request") @Valid JoinUserRequest request, @Nullable @RequestPart("image") MultipartFile file) {
        log.error(request.toString());
        log.error("join request's password {}", request.getPassword());
        User newUser = User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(request.getPassword())
                .build();
        try {
            userService.registerUser(newUser);
            log.info("[%s] %s has been registered.", newUser.getNickname(), newUser.getEmail());

            userService.addRoleToUser(newUser.getEmail(), "ROLE_USER");

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

    @PostMapping("/role/save")
    public ResponseEntity<ApiResponse> saveRole(@RequestBody @Valid Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        userService.saveRole(role);
        //return ResponseEntity.created(uri).body(role);

        return null;
    }

    @PostMapping("/role/addToUser")
    public ResponseEntity<ApiResponse> setUserRole(@RequestBody @Valid User user, @RequestBody @Valid Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());

        userService.addRoleToUser(user.getNickname(), role.getRoleName());
        //return ResponseEntity.created(uri).body(role);

        return null;
    }



    @DeleteMapping("/user/remove")
    public ResponseEntity<ApiResponse> removeUser(@RequestBody @Valid RemoveUserRequest request) {
        try {
            log.error("[UserApiController:removeUser]method executed");
            log.error("[UserApiController:removeUser]{}", request.toString());
            Long userId = request.getUserId();
            User findUser = userService.findUserById(userId);
            RemoveUserResponse response = RemoveUserResponse.builder()
                    .userId(findUser.getUserId())
                    .nickname(findUser.getNickname())
                    .build();

            int removedUserId = userService.removeUser(userId);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.DELETE_USER, response);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, null);
        }
    }

    @GetMapping("/user/search")
    public ResponseEntity<ApiResponse> searchUser(@RequestBody @Valid SearchUserRequest request) {
        try {
            User findUser = userService.findUserById(request.getUserId());

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


    @PatchMapping("/user/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestHeader(AUTHORIZATION) String header, @RequestPart("request") @Valid UpdateUserRequest request, @Nullable @RequestPart("image")MultipartFile file) {
        try {
            User updatedUser = userService.updateUser(request);
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

    @GetMapping("/user/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.findAll());
    }


    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("[UserApiController:refreshToken]method executed");
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            log.info("[UserApiController:refreshToken]authorizationHeader is valid");
            try {
                log.info("[UserApiController:refreshToken]try refresh token");
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String userEmail = decodedJWT.getSubject();
                User user = userService.findUserByEmail(userEmail);

                String accessToken = JWT.create()
                        .withSubject(user.getEmail()) // get email (security's username)
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000 )) // 10min
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

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
