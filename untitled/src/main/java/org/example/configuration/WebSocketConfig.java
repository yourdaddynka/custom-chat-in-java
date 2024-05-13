package org.example.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker // Включаем поддержку обработки сообщений WebSocket как брокера сообщений
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Метод для настройки брокера сообщений
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Включаем простой брокер сообщений для префикса "/user"
        config.enableSimpleBroker("/user");
        // Устанавливаем префикс для приложения, который будет использоваться в качестве пункта назначения сообщений
        config.setApplicationDestinationPrefixes("/app");
        // Устанавливаем префикс для адресов пользователей WebSocket
        config.setUserDestinationPrefix("/user");
    }

    // Метод для регистрации точек входа STOMP
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Добавляем точку входа WebSocket "/ws" с использованием SockJS
        registry.addEndpoint("/ws").withSockJS();
        // Добавляем точку входа WebSocket "/ws" с использованием пользовательского обработчика handshake и разрешаемые источники запросов
        registry.addEndpoint("/ws")
                .setHandshakeHandler(new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy()))
                .setAllowedOrigins("*");
    }

    // Метод для настройки преобразователей сообщений
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        // Создаем преобразователь сообщений JSON
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        // Устанавливаем ObjectMapper для преобразования JSON
        converter.setObjectMapper(new ObjectMapper());
        // Добавляем преобразователь в список преобразователей сообщений
        messageConverters.add(converter);
        // Возвращаем false, чтобы сообщить Spring, что другие конвертеры сообщений не должны быть настроены
        return false;
    }
}
