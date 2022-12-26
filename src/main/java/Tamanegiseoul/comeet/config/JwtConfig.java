package Tamanegiseoul.comeet.config;

import Tamanegiseoul.comeet.security.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${jwt.secrit}")
    private String accessTokenSicrit;

    @Value("${jwt.access-token-validity-in-seconds}")
    private Long accessTokenValidityInSeconds;

    // 액세스 토큰 발급용, 리프레시 토큰 발급용은 각각 별도의 키와 유효기간을 갖는다.
    @Bean
    public TokenProvider tokenProvider() {
        return new TokenProvider(accessTokenSicrit, accessTokenValidityInSeconds);
    }


}
