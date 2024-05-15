package org.example;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;
import java.util.Scanner;

public class WebSocketClient {

    public static void main(String[] args) {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        stompClient.connect("ws://localhost:8087/ws", sessionHandler);

        new Scanner(System.in).nextLine(); // Блокируем основной поток, чтобы соединение WebSocket не закрылось слишком быстро
    }

    static class MyStompSessionHandler extends StompSessionHandlerAdapter {

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.out.println("Connected to WebSocket");

            // Подписываемся на очередь ответов
            session.subscribe("/user/queue/response", new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return ResponseMessage.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    ResponseMessage response = (ResponseMessage) payload;
                    System.out.println("Received response: " + response);
                }
            });

            // Отправляем сообщение на создание комнаты
            session.send("/app/chat.createRoom/roomName", null);

            // Отправляем сообщение в комнату
            session.send("/app/chat.sendMessage/roomId", "Hello, world!");
        }
    }
}
