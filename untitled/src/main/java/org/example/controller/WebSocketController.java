package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.CustomException;
import org.example.model.Message;
import org.example.model.Room;
import org.example.model.dto.SenderDto;
import org.example.response.ResponseMessage;
import org.example.service.MessageService;
import org.example.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import java.security.Principal;

//@PreAuthorize("hasRole('ROLE_ADMIN')")
@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private RoomService roomService;
    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat.createRoom/{roomName}")
    @SendTo("/topic/queue/response")
    public Room createRoom(@DestinationVariable String roomName, Principal principal) throws CustomException {
        System.out.println("roomName == " + roomName + ", principal == " + principal);
        if (principal instanceof Authentication authentication) {
            if (authentication.getPrincipal() instanceof SenderDto senderDto) {
                String senderName = senderDto.getName();
                Room room = new Room(roomName, senderName);
                Room roomSaved = roomService.saveRoom(room);
//                System.out.println("----------------------------");
//                messagingTemplate.convertAndSend("/topic/queue/response", roomSaved);
                return roomSaved;
            }
        }
        throw new CustomException("/topic/error", "TOKEN_NOT_FOUND", null);
    }

    @MessageMapping("/chat.sendMessage/{roomId}")
    @SendTo("/queue/response")
    public ResponseMessage sendMessage(@Payload String message, @DestinationVariable String roomId, Principal principal) throws CustomException {
        System.out.println("message == " + message + ", roomId == " + roomId + ", principal == " + principal);
        if (principal instanceof Authentication authentication) {
            if (authentication.getPrincipal() instanceof SenderDto senderDto) {
                String userName = senderDto.getName();
                String userRole = senderDto.getRole();
                Room room = roomService.findById(roomId);
                Message messageUser = new Message(message, new SenderDto(userName, userRole), room);
                Message messageSaved = messageService.save(messageUser);
                System.out.println("----------------------------");
                return new ResponseMessage(messageSaved, true, null);
            }
        }
        throw new CustomException("/topic/error", "TOKEN_NOT_FOUND", null);
    }

}