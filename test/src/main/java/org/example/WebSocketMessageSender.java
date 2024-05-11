package org.example;


import org.springframework.web.socket.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.client.WebSocketClient;
import java.net.URI;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.util.concurrent.TimeUnit;

public class WebSocketMessageSender {

    public static void main(String[] args) throws Exception {
        // Создаем WebSocket клиент
        WebSocketClient webSocketClient = new StandardWebSocketClient();

        // Устанавливаем соединение с WebSocket сервером
        WebSocketSession webSocketSession = webSocketClient
                .doHandshake(new WebSocketHandler() {
                    @Override
                    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                        System.out.println("Соединение установлено: " + session.getId());
                        // Отправляем сообщение при успешном установлении соединения
                        String message = "Привет, мир!";
                        session.sendMessage(new TextMessage(message));
                    }

                    @Override
                    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                        System.out.println("Получено сообщение: " + message.getPayload());
                    }


                    @Override
                    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                        System.err.println("Ошибка транспорта на сессии " + session.getId() + ": " + exception.getMessage());
                    }

                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                        System.out.println("Соединение закрыто: " + session.getId() + ", Статус закрытия: " + closeStatus);
                    }

                    @Override
                    public boolean supportsPartialMessages() {
                        return false;
                    }
                }, String.valueOf(new URI("ws://localhost:8081/websocket")))
                .get(5, TimeUnit.SECONDS); // Устанавливаем таймаут ожидания

        // Закрываем соединение
        webSocketSession.close();
    }
}

