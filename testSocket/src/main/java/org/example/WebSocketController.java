package org.example;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class WebSocketController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(@Payload String message) throws Exception {
        System.out.println();
        System.out.println("Received message: " + message);
        System.out.println();
        return message;
    }

    @MessageMapping("/hello/aaa")
    @SendTo("/topic/greetings")
    public String aaaa(@Payload String message) throws Exception {
        System.out.println();
        System.out.println("Received message: " + message);
        System.out.println();
        return message;
    }
}
