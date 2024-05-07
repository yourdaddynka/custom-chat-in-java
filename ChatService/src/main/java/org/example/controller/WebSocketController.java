package org.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send-message")
    public void sendMessage(@Payload String message, SimpMessageHeaderAccessor accessor) {
        String token = accessor.getFirstNativeHeader("Authorization");
        messagingTemplate.convertAndSendToUser(accessor.getUser().getName(), "/topic/success", "successMessage"");
    }



}
