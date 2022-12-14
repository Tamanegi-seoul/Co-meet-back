package Tamanegiseoul.comeet.security;

import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.service.JwtCookieService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
//@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
     // in UsernamePasswordAuthenticationFilter, DEFAULT_ANT_PATH_REQUEST_MATCHER defined url for login as (POST, "/login")

    private final AuthenticationManager authenticationManager;
    private HashMap<String, String> jsonRequest;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        String password = super.getPasswordParameter();
        if(request.getHeader("Content-Type").equals(APPLICATION_JSON_VALUE)) {
            return jsonRequest.get(password);
        }
        return request.getParameter(password);
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        // co-meet will use email as username for login
        super.setUsernameParameter("email");
        String email = super.getUsernameParameter();
        if(request.getHeader("Content-Type").equals(APPLICATION_JSON_VALUE)) {
            return jsonRequest.get(email);
        }
        return request.getParameter(email);
    }



    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if(request.getHeader("Content-Type").equals(APPLICATION_JSON_VALUE)) {
            log.info("JSON LOGIN ATTEMP");

            ObjectMapper om = new ObjectMapper();
            try {
                this.jsonRequest = om.readValue(request.getReader().lines().collect(Collectors.joining()),
                        new TypeReference<HashMap<String, String>>() {});
            } catch (IOException e) {
                e.printStackTrace();
                throw new AuthenticationServiceException("Request Content-Type(application/json) Parsing Error");
            }
        }
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        log.info("{} attempt to login with {}", username, password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        return authenticate;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User)authentication.getPrincipal();

        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String accessToken = JWT.create()
                .withSubject(user.getUsername()) // get email (security's username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000 )) // 10min
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername()) // get email (security's username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000 )) // 30min
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        /*
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
         */

        /*
            need to return cookie with tokens
         */
        Cookie accessCookie = new Cookie("access_token", accessToken);
        accessCookie.setMaxAge(7 * 86400);
        accessCookie.setComment("access_token");
        //accessCookie.setHttpOnly(true);
        accessCookie.setPath(request.getContextPath());
        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setMaxAge(14 * 86400);
        refreshCookie.setComment("refresh_token");
        //refreshCookie.setHttpOnly(true);
        refreshCookie.setPath(request.getContextPath());

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);


        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("status_code", String.valueOf(HttpStatus.OK));
        responseBody.put("response_message", ResponseMessage.GENERATE_TOKEN);
        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }

}
