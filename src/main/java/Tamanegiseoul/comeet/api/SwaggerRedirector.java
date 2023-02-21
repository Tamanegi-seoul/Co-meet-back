package Tamanegiseoul.comeet.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
@RequestMapping("/api/usage")
public class SwaggerRedirector {
    @GetMapping
    public String getSwagger() {
        return "redirect:/swagger-ui/index.html";
    }
}
