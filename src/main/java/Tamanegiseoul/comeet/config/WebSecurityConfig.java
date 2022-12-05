package Tamanegiseoul.comeet.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CorsFilter corsFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilter(corsFilter) // @CrossOrigin(인증x), 시큐리티 필터에 등록 인증(O)
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

        return http.build();
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
