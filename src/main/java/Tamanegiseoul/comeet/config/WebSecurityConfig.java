package Tamanegiseoul.comeet.config;

import Tamanegiseoul.comeet.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

//@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors()
        .and()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .formLogin().disable()
        .httpBasic().disable()
        .authorizeRequests()
                .antMatchers("/api/user/validate").permitAll()
                .antMatchers("/api/user/join").permitAll()
                //.antMatchers("/api/user/remove").hasRole("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 로그인필요
                .antMatchers("/api/user/remove").hasAnyRole("USER", "MANAGER", "ADMIN") // 로그인필요
                .antMatchers("/api/user/search").permitAll() // 로그인필요
                .antMatchers("/api/user/update").hasAnyRole("USER", "MANAGER", "ADMIN") // 로그인필요

                .antMatchers("/api/post/register").hasAnyRole("USER", "MANAGER", "ADMIN") // 로그인필요
                .antMatchers("/api/post/update").hasAnyRole("USER", "MANAGER", "ADMIN") // 로그인필요
                .antMatchers("/api/post/search/**").permitAll()
                .antMatchers("/api/post/remove").hasAnyRole("USER", "MANAGER", "ADMIN") // 로그인필요

                .antMatchers("/api/comment/register").hasAnyRole("USER", "MANAGER", "ADMIN") // 로그인필요
                .antMatchers("/api/comment/update").hasAnyRole("USER", "MANAGER", "ADMIN") // 로그인필요
                .antMatchers("/api/comment/remove").hasAnyRole("USER", "MANAGER", "ADMIN") // 로그인필요
                .antMatchers("/api/comment/search").permitAll()

                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/logout").authenticated()
                .anyRequest().permitAll();

        http.addFilterAfter(
                jwtAuthenticationFilter,
                CorsFilter.class
        );
    }


    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("pasword")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}
