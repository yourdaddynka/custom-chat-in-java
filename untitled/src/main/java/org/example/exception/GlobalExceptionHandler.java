package org.example.exception;


import org.example.model.dto.SenderDto;
import org.example.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GlobalExceptionHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @ExceptionHandler(CustomException.class)
    @SendTo("/queue/response")
    public ResponseMessage handleCustomException(CustomException ex) {
        ex.getDestination();
        return new ResponseMessage(new SenderDto(ex.getSender(), null), false, ex.getMessage());
    }

}

