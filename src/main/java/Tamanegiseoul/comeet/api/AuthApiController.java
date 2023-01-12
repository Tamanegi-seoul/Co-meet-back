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

    @GetMapping("/token")
    @ApiOperation(value="토큰 갱신", notes="발행된 토큰 재발급")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());

                if(jwtProvider.validateToken(refreshToken)) {
                    log.info("TOKEN VALIDATE: TRUE");
                }else {
                    log.info("TOKEN VALIDATE: FALSE");
                }
                String memberEmail = jwtProvider.getUserEmail(refreshToken);
                Member findMember = memberService.findMemberByEmail(memberEmail);

                jwtProvider.generateAccessToken(findMember);
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                String accessToken = jwtProvider.generateAccessToken(findMember);
                refreshToken = jwtProvider.generateRefreshToken(memberEmail);

                Cookie accessCookie = new Cookie("accessToken", accessToken);
                accessCookie.setMaxAge(7 * 86400);
                accessCookie.setComment("accessToken");
                accessCookie.setHttpOnly(true);
                accessCookie.setPath(request.getContextPath());
                Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
                refreshCookie.setMaxAge(14 * 86400);
                refreshCookie.setComment("refreshToken");
                refreshCookie.setHttpOnly(true);
                refreshCookie.setPath(request.getContextPath());

                response.addCookie(accessCookie);
                response.addCookie(refreshCookie);


                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("statusCode", String.valueOf(HttpStatus.OK));
                responseBody.put("responseMessage", ResponseMessage.REFRESH_TOKEN);
                responseBody.put("accessToken", accessToken);
                responseBody.put("refreshToken", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), responseBody);


            }  catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN);
                //response.sendError(FORBIDDEN);
                Map<String, String> error = new HashMap<>();
                error.put("errorMessage", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }

    }
}
