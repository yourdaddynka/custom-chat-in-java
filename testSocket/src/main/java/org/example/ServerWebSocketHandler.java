package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

@Component
public class ServerWebSocketHandler implements WebSocketHandler {
    @Autowired
    private WebSocketController webSocketController;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket connection established: " + session.getId());
        // Здесь можно выполнить какие-то действия при успешном установлении соединения
    }


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("Received message from " + session.getId() + ": " + message.getPayload());
        String payload = (String) message.getPayload();
        System.out.println("payload -- " + payload);
        String response = webSocketController.greeting(payload); // Вызываем метод контроллера для обработки сообщения
        System.out.println("response -- " + response);
        session.sendMessage(new TextMessage(response)); // Отправляем ответ обратно клиенту
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("WebSocket error for session " + session.getId() + ": " + exception.getMessage());
        // Здесь можно обработать ошибку транспорта WebSocket
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("WebSocket connection closed: " + session.getId() + ", Close status: " + closeStatus);
        // Здесь можно выполнить какие-то действия при закрытии соединения
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
