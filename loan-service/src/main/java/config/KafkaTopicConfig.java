package config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
	
	@Bean
    public NewTopic loanCreatedTopic() {
        return TopicBuilder.name("loan-created").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic loanReturnedTopic() {
        return TopicBuilder.name("loan-returned").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic loanOverdueTopic() {
        return TopicBuilder.name("loan-overdue").partitions(1).replicas(1).build();
    }
}
