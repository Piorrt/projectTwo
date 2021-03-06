package pl.sages.javadevpro.projecttwo.config;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.external.env.kafka.KafkaTaskEnv;
import pl.sages.javadevpro.projecttwo.external.env.task.TaskEnv;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfiguration {

    @Bean
    public ProducerFactory<String, TaskEnv> taskProducerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }


    @Bean
    public KafkaTemplate<String, TaskEnv> taskKafkaTemplate() {
        return new KafkaTemplate<>(taskProducerFactory());
    }

    @Bean
    public ConsumerFactory<String, TaskEnv> taskConsumerFactory() {
        Map<String, Object> config = new HashMap<>();


        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_json");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");


        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
                new JsonDeserializer<>(TaskEnv.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskEnv> taskKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TaskEnv> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(taskConsumerFactory());
        return factory;
    }

    @Bean
    public KafkaTaskEnv kafkaUserTaskEnv(KafkaTemplate<String, TaskEnv> kafkaTemplate){
        return new KafkaTaskEnv(kafkaTemplate);
    }
}
