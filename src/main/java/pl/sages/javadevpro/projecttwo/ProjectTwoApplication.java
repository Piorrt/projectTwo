package pl.sages.javadevpro.projecttwo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import pl.sages.javadevpro.projecttwo.domain.usertask.TaskStatus;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;
import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskEnv;
import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskStatusEnv;

@SpringBootApplication
public class ProjectTwoApplication {



	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ProjectTwoApplication.class, args);


		UserTaskEnv userTaskEnv = new UserTaskEnv(
				"12",
				"Task2",
				"super fajny task",
				"folder tasku",
				UserTaskStatusEnv.STARTED,
				"marcin@gmail.com"
		);


		final KafkaTemplate<String, UserTaskEnv> kafkaTemplate = context.getBean(KafkaTemplate.class);
		kafkaTemplate.send("Kafka_Task_json", userTaskEnv);
		kafkaTemplate.send("Kafka_Task_json", userTaskEnv);
		kafkaTemplate.send("Kafka_Task_json", userTaskEnv);
		kafkaTemplate.send("Kafka_Task_json", userTaskEnv);
		kafkaTemplate.send("Kafka_Task_json", userTaskEnv);
		kafkaTemplate.send("Kafka_Task_json", userTaskEnv);


//		final UserTaskExecutorAdapter userTaskExecutorAdapter = context.getBean(UserTaskExecutorAdapter.class);
//		userTaskExecutorAdapter.exec(userTask2);



// FIXME  add admin to app

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
