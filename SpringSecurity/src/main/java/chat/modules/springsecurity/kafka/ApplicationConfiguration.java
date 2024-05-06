package chat.modules.springsecurity.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.JacksonUtils;

@Configuration
@PropertySource("classpath:kafka.properties")
public class ApplicationConfiguration {
    private final String topicName;

    @Bean
    public String getTopicName() {
        return topicName;
    }

    @Autowired
    public ApplicationConfiguration(@Value("${kafka.topic.read.name}") String topicName) {
        this.topicName = topicName;
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(topicName).partitions(1).replicas(1).build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }
}
