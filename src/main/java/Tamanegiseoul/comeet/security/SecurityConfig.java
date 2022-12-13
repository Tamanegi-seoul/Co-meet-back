package Tamanegiseoul.comeet.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login"); // overwrite default url
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/login/**", "/api/token/refresh/**").permitAll();
        http.authorizeRequests().antMatchers("/api/user/validate").permitAll();
        http.authorizeRequests().antMatchers("/api/user/join").permitAll();
        http.authorizeRequests().antMatchers("/api/user/search").permitAll();

        http.authorizeRequests().antMatchers("/api/post/search/**").permitAll();
        http.authorizeRequests().antMatchers("/api/comment/search/**").permitAll();
//
//        http.authorizeRequests().antMatchers("/api/user/remove").hasAnyAuthority("ROLE_USER","ROLE_ADMIN");
//        http.authorizeRequests().antMatchers("/api/user/remove").authenticated();
//        http.authorizeRequests().antMatchers("/api/user/update").hasAnyAuthority("ROLE_USER","ROLE_ADMIN");
//
//        http.authorizeRequests().antMatchers("/api/post/register").hasAnyAuthority("ROLE_USER","ROLE_ADMIN");
//        http.authorizeRequests().antMatchers("/api/post/update").hasAnyAuthority("ROLE_USER","ROLE_ADMIN");
//        http.authorizeRequests().antMatchers("/api/post/remove").hasAnyAuthority("ROLE_USER","ROLE_ADMIN");
//
//        http.authorizeRequests().antMatchers("/api/comment/register").hasAnyAuthority("ROLE_USER","ROLE_ADMIN");
//        http.authorizeRequests().antMatchers("/api/comment/update").hasAnyAuthority("ROLE_USER","ROLE_ADMIN");
//        http.authorizeRequests().antMatchers("/api/comment/remove").hasAnyAuthority("ROLE_USER","ROLE_ADMIN");

        http.authorizeRequests().antMatchers("/api/role/**").hasAnyAuthority("ROLE_ADMIN");

        http.authorizeRequests().anyRequest().permitAll();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
