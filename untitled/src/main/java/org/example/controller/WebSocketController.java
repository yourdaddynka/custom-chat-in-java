package org.example.controller;


import org.example.exception.CustomException;
import org.example.model.Message;
import org.example.model.Room;
import org.example.model.dto.SenderDto;
import org.example.security.jwt.JwtTokenProvider;
import org.example.service.MessageService;
import org.example.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

import static java.lang.System.out;

//@PreAuthorize("hasRole('ROLE_ADMIN')")
@Controller
public class WebSocketController {

    @Autowired
    private  RoomService roomService;
    @Autowired
    private  MessageService messageService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    // Аннотация @MessageMapping указывает на метод, который обрабатывает входящие сообщения по указанному адресу
    @MessageMapping("/hello")
    // Аннотация @SendTo указывает, куда отправлять ответное сообщение
    @SendTo("/topic/greetings")
    public String greeting() throws Exception {
        // Просто формируем ответное сообщение, здесь может быть любая ваша логика обработки
        return"hello";
    }


//    @MessageMapping("/chat.createRoom")
//    @SendTo("/topic/public")
//    public void createRoom(@RequestParam("roomName") String roomName, Principal principal) {
//        out.println(roomName);
//        if (principal instanceof Authentication) {
//            Authentication authentication = (Authentication) principal;
//            String jwtToken = (String) authentication.getCredentials();
//            String senderName = jwtTokenProvider.getUsernameFromToken(jwtToken);
//            Room room = new Room(roomName, senderName);
//            roomService.saveRoom(room);
//            messagingTemplate.convertAndSend("/topic/room/" + room.getId(), room);
//        }
//
//        messagingTemplate.convertAndSend("/topic/", "error");
//    }
//
//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public void sendMessage(@Payload String message, @DestinationVariable String roomId, Principal principal) throws CustomException {
//        if (principal instanceof Authentication) {
//            Authentication authentication = (Authentication) principal;
//            String jwtToken = (String) authentication.getCredentials();
//            String userName = jwtTokenProvider.getUsernameFromToken(jwtToken);
//            String userRole = jwtTokenProvider.getRoleFromToken(jwtToken);
//            Room room = roomService.findById(roomId);
//            Message messageUser = new Message(message, new SenderDto(userName, userRole), room);
//            messageService.save(messageUser);
//            messagingTemplate.convertAndSend("/topic/room/" + roomId, messageUser);
//        }
//        messagingTemplate.convertAndSend("/topic/room/" + roomId, "error");
//    }

}
