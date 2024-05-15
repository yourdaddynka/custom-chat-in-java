package org.example.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends Exception {
    private final String destination;
    private final String sender;
    private final String message;
    public CustomException(String destination, String message, String sender) {
        super(message);
        this.destination = destination;
        this.message = message;
        this.sender = sender;
    }
}