package org.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WebSocketTest {
    public static void main(String[] args) {
        try {
            // Устанавливаем URI для подключения к серверу WebSocket
            URI uri = new URI("ws://localhost:8081/websocket");

            // Создаем экземпляр WebSocket-клиента
            WebSocketClient client = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    System.out.println("Connected to server");
                }

                @Override
                public void onMessage(String s) {
                    System.out.println("Received message: " + s);
                    // Закрываем соединение после получения сообщения
                    close();
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    System.out.println("Connection closed " + s);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            };

            // Подключаемся к серверу WebSocket
            client.connectBlocking();

            // Ждем, пока соединение не закроется
            CountDownLatch latch = new CountDownLatch(1);
            latch.await(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
