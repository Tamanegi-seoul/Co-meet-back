package Tamanegiseoul.comeet.security;

import Tamanegiseoul.comeet.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;
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
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(), memberService);
        customAuthenticationFilter.setFilterProcessesUrl("/api/auth/login"); // overwrite default url

        http.cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/*").permitAll()

                    .antMatchers("/swagger-ui.html").permitAll()
                    .antMatchers("/api/auth/login/**", "/api/auth/token/refresh/**").permitAll()
                    .antMatchers("/api/member/validate/**").permitAll()
                    .antMatchers("/api/member/join/**").permitAll()
                    .antMatchers("/api/member/search/**").permitAll()
                    .antMatchers("/api/member/update/**").permitAll()
                    .antMatchers("/api/member/remove/**").permitAll()
                    .antMatchers("/api/post/search/**").permitAll()
                    .antMatchers("/api/comment/search/**").permitAll()

                    .antMatchers("/api/member/remove/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                    .antMatchers("/api/member/remove/**").authenticated()
                    .antMatchers("/api/member/update/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                    .antMatchers("/api/post/register/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                    .antMatchers("/api/post/update/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                    .antMatchers("/api/post/remove/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                    .antMatchers("/api/comment/register/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                    .antMatchers("/api/comment/update/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                    .antMatchers("/api/comment/remove/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                    .antMatchers("/api/auth/role/**").hasAnyAuthority("ROLE_ADMIN")
                    .anyRequest().permitAll()
                        .and()
                    .addFilter(customAuthenticationFilter)
                        .exceptionHandling()
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .and()
                    .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                        .exceptionHandling()
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    
}
