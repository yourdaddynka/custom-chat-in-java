package chat.modules.springsecurity.kafka;

import chat.modules.springsecurity.model.MessageModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.Map;


@Configuration
@PropertySource("classpath:kafka.properties")
public class ApplicationConsumer {
    @Autowired
    private KafkaProperties kafkaProperties;
    @Autowired public ObjectMapper objectMapper;
    @Value("${kafka.server}") private String kafkaServer;

    @Value("${kafka.group.id}") private String kafkaGroupId;
    @Bean
    public Map<String, Object> consumerConfig(){
        Map<String,Object> props = kafkaProperties.buildConsumerProperties(null);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,10);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,1_000);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,kafkaGroupId);
        return props;
    }

    @Bean
    public ConsumerFactory<String, MessageModel> consumerFactory(){
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    @Bean("listenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,MessageModel>> listenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String,MessageModel> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        factory.getContainerProperties().setIdleBetweenPolls(1_000);
        factory.getContainerProperties().setPollTimeout(1_000);
        var executor = new SimpleAsyncTaskExecutor("k-consumer-");
        executor.setConcurrencyLimit(10);
        var listenerTaskExecutor = new ConcurrentTaskExecutor(executor);
        factory.getContainerProperties().setListenerTaskExecutor(listenerTaskExecutor);
        return factory;
    }

}
