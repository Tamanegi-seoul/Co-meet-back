package Tamanegiseoul.comeet.config;

import Tamanegiseoul.comeet.interceptor.AuthValidInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@Slf4j
public class WebConfiguration implements WebMvcConfigurer {

    private AuthValidInterceptor authValidInterceptor;

    private List<String> paths;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("add interceptors on {}", paths.toString());
        registry.addInterceptor(authValidInterceptor).addPathPatterns(paths);
    }

    public WebConfiguration(AuthValidInterceptor authValidInterceptor) {
        log.info("WebMvcConfigurer");
        this.authValidInterceptor = authValidInterceptor;
        this.paths = new ArrayList<String>(
                Arrays.asList("/api/member", "/api/post", "/api/comment")
        );

    }
}
