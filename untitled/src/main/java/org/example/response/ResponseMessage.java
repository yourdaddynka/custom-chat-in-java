package org.example.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.example.model.dto.SenderDto;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessage {
    @JsonProperty("sender")
    private SenderDto sender;

    @JsonProperty("message")
    private String message;

    public ResponseMessage(String message,  SenderDto sender) {
        this.message = message;
        this.sender = sender;
    }

}