package pl.sages.javadevpro.projecttwo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;

@SpringBootApplication
public class ProjectTwoApplication {



	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ProjectTwoApplication.class, args);

		UserTask userTask = new UserTask("Task1", "example@gmail.com", "/home/raggy2k4/Dokumenty/task1", "locked");
		UserTask userTask2 = new UserTask("Task2", "example@gmail.com", "/home/raggy2k4/Dokumenty/task1", "locked");
		UserTask userTask3 = new UserTask("Task3", "example@gmail.com", "/home/raggy2k4/Dokumenty/task1", "locked");
		UserTask userTask4 = new UserTask("Task4", "example@gmail.com", "/home/raggy2k4/Dokumenty/task1", "locked");
		UserTask userTask5 = new UserTask("Task5", "example@gmail.com", "/home/raggy2k4/Dokumenty/task1", "locked");


		final KafkaTemplate<String, UserTask> kafkaTemplate = context.getBean(KafkaTemplate.class);
		kafkaTemplate.send("Kafka_Task_json", userTask);

//		final UserTaskExecutorAdapter userTaskExecutorAdapter = context.getBean(UserTaskExecutorAdapter.class);
//		userTaskExecutorAdapter.exec(userTask2);


//		final UserRepository userRepository =
//				context.getBean(UserRepository.class);
//
//		userRepository.save(
//			new User(
//				"jan@example.com",
//				"Jan Kowalski",
//				"MyPassword",
//				List.of("ADMIN", "STUDENT")
//			)
//		);
//		userRepository.save(
//			new User(
//				"stefan@example.com",
//				"Stefan Burczymucha",
//				"password",
//				List.of("STUDENT")
//			)
//		);
	}

}
