package org.example.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.example.model.dto.SenderDto;

@Getter
@Setter
public class ResponseMessage {
    private Object object;
    private Boolean successes;
    private String message;

    public ResponseMessage(Object object, Boolean successes, String message) {
        this.object = object;
        this.successes = successes;
        this.message = message;
    }
}