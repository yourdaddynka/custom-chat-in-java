package chat.modules.springsecurity.kafka;

import chat.modules.springsecurity.model.MessageModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
@PropertySource("classpath:kafka.properties")
public class ApplicationProducer {
    @Autowired private KafkaProperties kafkaProperties;
    @Autowired public ObjectMapper objectMapper;
    @Value("${kafka.server}") private String kafkaServer;
    @Value("${kafka.producer.id}") private String kafkaProducerId;
    @Bean
    public Map<String, Object> producerConfig() {
        Map<String, Object> props = kafkaProperties.buildProducerProperties(null);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaProducerId);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG,kafkaServer);
        return props;
    }
    @Bean
    public ProducerFactory<String, MessageModel> producerFactory() {
        DefaultKafkaProducerFactory<String, MessageModel> kafkaProduct = new DefaultKafkaProducerFactory<>(producerConfig());
        kafkaProduct.setValueSerializer(new JsonSerializer<>(objectMapper));
        return kafkaProduct;
    }
    @Bean
    public KafkaTemplate<String, MessageModel> kafkaTemplate() {
        KafkaTemplate<String, MessageModel> template = new KafkaTemplate<>(producerFactory());
        template.setMessageConverter(new StringJsonMessageConverter());
        return template;
    }
}
