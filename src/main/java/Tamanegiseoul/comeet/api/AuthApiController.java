package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Role;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.security.JwtProvider;
import Tamanegiseoul.comeet.service.MemberService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static Tamanegiseoul.comeet.dto.StatusCode.FORBIDDEN;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth API", description = "인증/인가 관련 API 제공")
public class AuthApiController {

    @Autowired
    private JwtProvider jwtProvider;
    private final MemberService memberService;

    @PostMapping("/role/save")
    @ApiOperation(value="권한 등록", notes="신규 권한 등록")
    public ResponseEntity<ApiResponse> saveRole(@RequestBody @Valid Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        memberService.saveRole(role);
        //return ResponseEntity.created(uri).body(role);

        return null;
    }

    @PostMapping("/role/addToUser")
    @ApiOperation(value="권한 지정", notes="기존 회원 권한 지정")
    public ResponseEntity<ApiResponse> setUserRole(@RequestBody @Valid Member member, @RequestBody @Valid Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());

        memberService.addRoleToMember(member.getNickname(), role.getRoleName());
        //return ResponseEntity.created(uri).body(role);

        return null;
    }

    @GetMapping("/token/refresh")
    @ApiOperation(value="토큰 갱신", notes="발행된 토큰 재발급")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("[MemberApiController:refreshToken]method executed");
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            log.info("[MemberApiController:refreshToken]authorizationHeader is valid");
            try {
                log.info("[MemberApiController:refreshToken]try refresh token");
                String refreshToken = authorizationHeader.substring("Bearer ".length());

                if(jwtProvider.validateToken(refreshToken)) {
                    log.info("TOKEN VALIDATE: TRUE");
                }else {
                    log.info("TOKEN VALIDATE: FALSE");
                }
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

                Cookie accessCookie = new Cookie("access_token", accessToken);
                accessCookie.setMaxAge(7 * 86400);
                accessCookie.setComment("access_token");
                accessCookie.setHttpOnly(true);
                accessCookie.setPath(request.getContextPath());
                Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
                refreshCookie.setMaxAge(14 * 86400);
                refreshCookie.setComment("refresh_token");
                refreshCookie.setHttpOnly(true);
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
