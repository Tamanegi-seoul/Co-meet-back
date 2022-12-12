package Tamanegiseoul.comeet.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private Algorithm algorithm;
    private long validityInMilliseconds;

    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") String secretKey, @Value("${security.jwt.token.expire-length}") Long validityInMilliseconds) {
        this.algorithm = Algorithm.HMAC256("secret".getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
    }

    // generate token
    public String generateAccessToken(User user, String subject) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+ 10 * 60 * 1000 ))
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String generateRefreshToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername()) // get email (security's username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000 )) // 30min
                .sign(algorithm);
    }

}
