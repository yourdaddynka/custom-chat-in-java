package org.example.controller;

import org.example.exception.CustomException;
import org.example.jwt.JwtTokenProvider;
import org.example.model.Message;
import org.example.model.Room;
import org.example.model.dto.SenderDto;
import org.example.service.MessageService;
import org.example.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
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


    @MessageMapping("/chat.createRoom/{roomName}")
    @SendTo("/topic/public")
    public void createRoom( @org.springframework.messaging.handler.annotation.DestinationVariable String roomName, Principal principal) throws CustomException {
        out.println("roomName == " + roomName + ", principal == " + principal);
        if (principal instanceof Authentication) {
            Authentication authentication = (Authentication) principal;
            if (authentication.getPrincipal() instanceof SenderDto) {
                SenderDto senderDto = (SenderDto) authentication.getPrincipal();
                String senderName = senderDto.getName();
                Room room = new Room(roomName, senderName);
                Room roomSaved = roomService.saveRoom(room);
                out.println("roomSaved = " + roomSaved.getId() + " " + roomSaved.getName());
                messagingTemplate.convertAndSend("/topic/room/" + roomSaved.getId(), roomSaved);
            }
        }
        messagingTemplate.convertAndSend("/topic/", "error");
    }

    @MessageMapping("/chat.sendMessage/{roomId}")
    @SendTo("/topic/public")
    public void sendMessage(@Payload String message,  @org.springframework.messaging.handler.annotation.DestinationVariable String roomId, Principal principal) throws CustomException {
        out.println("message == " + message + ", roomId == " + roomId + ", principal == " + principal);
        if (principal instanceof Authentication) {
            Authentication authentication = (Authentication) principal;
            if (authentication.getPrincipal() instanceof SenderDto) {
                SenderDto senderDto = (SenderDto) authentication.getPrincipal();
                String userName = senderDto.getName();
                String userRole = senderDto.getRole();
                out.println("userName == " + userName + " userRole == " + userRole);
                Room room = roomService.findById(roomId);
                out.println("room == " + room.getName());
                Message messageUser = new Message(message, new SenderDto(userName, userRole), room);

                messageService.save(messageUser);
                messagingTemplate.convertAndSend("/topic/room/" + roomId, messageUser);
            }
            messagingTemplate.convertAndSend("/topic/room/" + roomId, "error");
        }
    }
}
