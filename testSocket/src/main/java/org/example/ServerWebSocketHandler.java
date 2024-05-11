package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
public class ServerWebSocketHandler implements WebSocketHandler {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket connection established: " + session.getId());
        // Здесь можно выполнить какие-то действия при успешном установлении соединения
    }


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = (String) message.getPayload();
        String uriPath = session.getUri().getPath();
        String acceptedProtocol = uriPath.substring(uriPath.indexOf("websocket") + "websocket".length());

//        System.out.println("acceptedProtocol -- " + acceptedProtocol);
//        System.out.println("Received message from " + session.getId() + ": " + message.getPayload());
//        System.out.println("payload -- " + payload);


        System.out.println("acceptedProtocol -- " + acceptedProtocol + " payload -- " + payload);
        String destination = "/topic" + acceptedProtocol;
        System.out.println("destination -- " + destination);
        messagingTemplate.convertAndSend(destination, payload);
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
