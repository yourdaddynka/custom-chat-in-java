package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {


    // Аннотация @MessageMapping указывает на метод, который обрабатывает входящие сообщения по указанному адресу
    @MessageMapping("/hello")
    // Аннотация @SendTo указывает, куда отправлять ответное сообщение
    @SendTo("/topic/greetings")
    public String greeting() throws Exception {
        // Просто формируем ответное сообщение, здесь может быть любая ваша логика обработки
        return "hello";
    }
}