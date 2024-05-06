package chat.modules.springsecurity.service.imp;

import chat.modules.springsecurity.model.MessageEntity;
import chat.modules.springsecurity.model.MessageModel;
import chat.modules.springsecurity.repository.MessageEntityRepository;
import chat.modules.springsecurity.service.MessageModelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageModelServiceImpl implements MessageModelService {
    private static final Logger log = LoggerFactory.getLogger(MessageModelServiceImpl.class);
    private MessageEntityRepository repository;
    private KafkaTemplate<String, MessageModel> modelKafkaTemplate;
    private ObjectMapper objectMapper;

    @Value("${kafka.topic.write.name}") private String outputTopicName;

    @Autowired
    public MessageModelServiceImpl(KafkaTemplate<String, MessageModel> modelKafkaTemplate, ObjectMapper objectMapper ) {
        this.modelKafkaTemplate = modelKafkaTemplate;
        this.objectMapper = objectMapper;

    }

    @Override
    public void save(MessageModel message) {
        MessageEntity entity = new MessageEntity();
        entity.setMessage(message.getMessage());
        entity.setRoomName(message.getRoomName());
        entity.setSenderName(message.getSenderName());
        entity.setTimestamp(message.getTimestamp());
        repository.save(entity);
    }

    @Override
    public void send(MessageModel message) {
        log.info("<= sending {}", writeValueAsString(message));
        modelKafkaTemplate.send(outputTopicName, message);
    }

    private String writeValueAsString(MessageModel entity) {
        try {
            return objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Writing value to JSON failed: " + entity.toString());
        }
    }

    @Override
    @KafkaListener(
            id = "MessageModel",
            topics = "${kafka.topic.read.name}",
            containerFactory = "listenerContainerFactory"
    )
    public void consume(MessageModel message) {
        log.info("Received Message: {}", message);
        save(message);
    }
}
