package Tamanegiseoul.comeet.security;

import Tamanegiseoul.comeet.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;


@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(), memberService, jwtProvider);
        customAuthenticationFilter.setFilterProcessesUrl("/api/auth/login"); // overwrite default url

        http.cors()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()

                .antMatchers(OPTIONS, "/*").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/api/auth/login/**", "/api/auth/token/**").permitAll()

                .antMatchers("/api/member/profile").permitAll()
                .antMatchers(GET,"/api/member").authenticated()
                .antMatchers(POST,"/api/member").permitAll() // sign up
                .antMatchers(PATCH,"/api/member").authenticated()
                .antMatchers(DELETE,"/api/member/**").authenticated()

                .antMatchers(GET,"/api/comment/**").permitAll()
                .antMatchers(POST,"/api/comment").authenticated()
                .antMatchers(PATCH,"/api/comment/**").authenticated()
                .antMatchers(DELETE,"/api/comment/**").authenticated()


                .antMatchers("/api/post/all/**").permitAll()
                .antMatchers(GET,"/api/post/my/**").permitAll()
                .antMatchers(GET,"/api/post/**").permitAll()
                .antMatchers(POST,"/api/post/**").authenticated()
                .antMatchers(PATCH,"/api/post/**").authenticated()
                .antMatchers(DELETE,"/api/post/**").authenticated()

                .antMatchers("/api/auth/role/**").hasAnyAuthority("ROLE_ADMIN")

                .anyRequest().permitAll()
                    .and()
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .addFilterAfter(new CustomAuthorizationFilter(jwtProvider), CustomAuthenticationFilter.class)
                    .exceptionHandling()
                    .accessDeniedHandler(jwtAccessDeniedHandler)
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    
}
