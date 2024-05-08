package org.example.exception;


import org.example.model.dto.SenderDto;
import org.example.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GlobalExceptionHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @ExceptionHandler(CustomException.class)
    public void handleCustomException(CustomException ex) {
        ResponseMessage response = new ResponseMessage(ex.getMessage(), new SenderDto(ex.getSender(), null));
//        messagingTemplate.convertAndSend(ex.getDestination(), response);
    }

}

