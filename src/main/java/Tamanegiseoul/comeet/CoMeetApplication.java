package Tamanegiseoul.comeet;

import Tamanegiseoul.comeet.domain.Role;
import Tamanegiseoul.comeet.domain.User;
import Tamanegiseoul.comeet.service.UserService;
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
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveRole(Role.builder().roleName("ROLE_USER").build());
			userService.saveRole(Role.builder().roleName("ROLE_MANAGER").build());
			userService.saveRole(Role.builder().roleName("ROLE_ADMIN").build());
			//userService.saveRole(Role.builder().roleName("ROLE_SUPER_ADMIN").build());

			userService.registerUser(
					User.builder()
							.email("admin")
							.nickname("관리자")
							.password("password")
							.build()
			);

			userService.addRoleToUser("admin", "ROLE_ADMIN");
		};
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
