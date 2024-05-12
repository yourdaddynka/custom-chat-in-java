package org.example;

import org.springframework.http.HttpHeaders;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Collections;

public class WebSocketMessageSender {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        MyStompSessionHandler sessionHandler = new MyStompSessionHandler();

        // Создаем заголовки запроса
        StompHeaders headers = new StompHeaders();
        headers.add("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiRU1QTE9ZRUUiLCJzdWIiOiJyaG9nb3JvbiIsImlhdCI6MTcxNTAwNDYyMCwiZXhwIjoxNzE1MDkxMDIwfQ.pMlbIVJdUzhMftHURecsXjv0c6IdweuQiVKbGZV4yT4D-RV7gpwOskOfGzoTCh40au4nilcfrMBLmT0jQ1uP7g");

        String wsUrl = "ws://localhost:8081/ws";
        stompClient.connect(wsUrl, sessionHandler, headers);

        Thread.sleep(5000); // Ждем несколько секунд, чтобы установить соединение

        StompSession session = sessionHandler.getSession();
        session.send("/app/chat.createRoom/MyRoom", null);

        String chatMessage = "сообщение для чата";
        String jsonMessage = objectMapper.writeValueAsString(chatMessage);
        session.send("/app/chat.sendMessage/1", jsonMessage.getBytes());
    }

    private static class MyStompSessionHandler extends StompSessionHandlerAdapter {
        private StompSession session;

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            this.session = session;
        }

        public StompSession getSession() {
            return session;
        }
    }
}
