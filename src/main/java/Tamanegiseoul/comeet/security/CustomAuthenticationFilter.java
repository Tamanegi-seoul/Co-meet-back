package Tamanegiseoul.comeet.security;

import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.service.MemberService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
     // in UsernamePasswordAuthenticationFilter, DEFAULT_ANT_PATH_REQUEST_MATCHER defined url for login as (POST, "/login")

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;
    private HashMap<String, String> jsonRequest;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, MemberService memberService, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
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

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // below for request json parser
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

        Member findMember = memberService.findMemberByEmail(user.getUsername());

        String accessToken = jwtProvider.generateAccessToken(findMember);
        String refreshToken = jwtProvider.generateRefreshToken(findMember.getEmail());

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

        /*
            basically, tokens should be delivered via Cookie to prevent XSS attack etc.
            Now, front-end team has not been deployed React Application, so front app IP is not fixed yet.
            Because of this reason, it is impossible to use Set-Cookie.
            Temporally, use response body to deliver the generaed token.
         */

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("status_code", String.valueOf(HttpStatus.OK));
        responseBody.put("response_message", ResponseMessage.GENERATE_TOKEN);
        responseBody.put("access_token", accessToken);
        responseBody.put("refresh_token", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);


        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }

}
