package org.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class WebSocketConfig {

    @Bean
    public SimpMessagingTemplate messagingTemplate(MessageChannel messageChannel) {
        return new SimpMessagingTemplate(messageChannel);
    }

    @Bean
    public MessageChannel messageChannel() {
        return (MessageChannel) new QueueChannel(); // Пример, можете выбрать подходящий канал для ваших нужд
    }

}
