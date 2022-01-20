package pl.sages.javadevpro.projecttwo.api;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskEnv;
import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskStatusEnv;

@AllArgsConstructor
@RestController
@RequestMapping("/kafka")
public class TestEndpoint {
    private KafkaTemplate<String, UserTaskEnv> kafkaTemplate;

    private static final String TOPIC = "Kafka_Task_json";

    @GetMapping("/sendtask/{taskId}")
    @Secured("ROLE_STUDENT")
    public String post(@PathVariable("taskId") final String taskId) {


        kafkaTemplate.send(TOPIC, new UserTaskEnv(
                taskId,
                "name",
                "description",
                "/Users/rafalnowak/WebstormProjects/task1",
                UserTaskStatusEnv.SUBMITTED,
                "example@gmail.com"));

        return "Task Sent Successfully";
    }
}