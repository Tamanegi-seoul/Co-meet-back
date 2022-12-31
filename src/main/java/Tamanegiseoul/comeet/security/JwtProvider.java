package Tamanegiseoul.comeet.security;

import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Role;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    private Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds; // default set as 60min

    public JwtProvider(@Value("${security.jwt.token.secret-key}") String secretKey, @Value("${security.jwt.token.expire-length}") Long validityInMilliseconds) {
        this.algorithm = Algorithm.HMAC256(secretKey.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
    }

    // generate token
    public String generateAccessToken(Member member) {
        return JWT.create()
                .withSubject(member.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+ (validityInMilliseconds / 60))) // 3 min
                .withClaim("roles", member.getRoles().stream().map(Role ::getRoleName).collect(Collectors.toList()))
                .withClaim("nickname", member.getNickname())
                .withClaim("member_id", member.getMemberId())
                .sign(algorithm);
    }

    public String generateRefreshToken(String email) {
        return JWT.create()
                .withSubject(email) // get email (security's username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000 )) // 24hr
                .sign(algorithm);
    }

    // JWT 토큰에서 인증 정보 조회
    public boolean getAuthentication(String token, String email) {
        String subject = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody().getSubject();

        return subject.equals(email);
    }

    // 토큰에서 회원 email 추출
    public String getUserEmail(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String token) throws ExpiredJwtException {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature => {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token => {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token => {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty. => {}", e.getMessage());
        }
        return false;
    }

    // Get AccessToken from request's cookie
    public String getAccessToken(HttpServletRequest request) {
        return request.getCookies()[0].getValue();
    }

    // Get RefreshToken from request's cookie
    public String getRefreshToken(HttpServletRequest request) {
        return request.getCookies()[1].getValue();
    }



}
