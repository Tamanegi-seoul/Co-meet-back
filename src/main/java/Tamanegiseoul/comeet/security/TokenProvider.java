package Tamanegiseoul.comeet.security;

import Tamanegiseoul.comeet.domain.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider  {

    private static final String SECRIT_KEY = "NMA8JPctFuna59f5NMA8JPctFuna59f5NMA8JPctFuna59f5";

    public String create(Users user) {
        Date expiryDate = Date.from (
                Instant.now()
                        .plus(1, ChronoUnit.DAYS)
        );

        byte[] keyBytes = Decoders.BASE64.decode(SECRIT_KEY);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        //jwtBuilder.signWith(key); //or signWith(Key, SignatureAlgorithm)

        return Jwts.builder()
                .signWith(key)
                .setSubject(Long.toString(user.getUserId()))
                .setIssuer("co-meet-back") // iss
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    public boolean validateToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRIT_KEY)
                .parseClaimsJws(token)
                .getBody();

        if(claims.getExpiration().compareTo(Date.from(Instant.now())) < 0) {
            // 만료된 경우
            return false;
        } else {
            return true;
        }
    }

    public String validateAndGetUserId(String token) {
        // praseClaimsJws 메소드가 Base64로 디코딩 및 파싱
        // 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용해 서명한 후 token의 서명과 비교
        // 위조되지 않았다면 페이로드(Claims)리턴, 위조라면 예외를 날림
        // 그 중 우리는 userId가 필요함으로 getBody를 호출
        Claims claims = Jwts.parser()
                .setSigningKey(SECRIT_KEY)
                .parseClaimsJws(token)
                .getBody();


        return claims.getSubject();
        //return claims.getSubject(); // subject는 우리가 원하는 사용자의 아이디를 뜻함
    }


}
