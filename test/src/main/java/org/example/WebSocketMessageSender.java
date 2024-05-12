package org.example;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

public class WebSocketMessageSender {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static void main(String[] args) throws Exception {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        MyStompSessionHandler sessionHandler = new MyStompSessionHandler();

        // Установка авторизационных данных, если это необходимо
        // HttpHeaders headers = new HttpHeaders();
        // headers.add("Authorization", "Bearer your_access_token");

        stompClient.connect("ws://localhost:8081/ws", sessionHandler);

        Thread.sleep(5000); // Ждем несколько секунд, чтобы установить соединение

        StompSession session = sessionHandler.getSession();
        session.send("/app/messages/senderId/recipientId/count", null);
        String chatMessage = "сообщение для чата";
        String jsonMessage = objectMapper.writeValueAsString(chatMessage);
        session.send("/app/chat", jsonMessage.getBytes());
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
