package Tamanegiseoul.comeet;

import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Role;
import Tamanegiseoul.comeet.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CoMeetApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoMeetApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
