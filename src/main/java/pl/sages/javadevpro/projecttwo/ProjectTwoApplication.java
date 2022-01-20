package pl.sages.javadevpro.projecttwo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.user.UserRepository;

import java.util.List;

@SpringBootApplication
public class ProjectTwoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ProjectTwoApplication.class, args);

		final UserRepository userRepository =
				context.getBean(UserRepository.class);
//
//		userRepository.save(
//			new User(
//				"jan@example.com",
//				"Jan Kowalski",
//				"MyPassword",
//				List.of("ADMIN"),
//				null
//			)
//		);
//
//		userRepository.save(
//			new User(
//				"stefan@example.com",
//				"Stefan Burczymucha",
//				"password",
//				List.of("STUDENT"),
//				null
//			)
//		);
	}

}
