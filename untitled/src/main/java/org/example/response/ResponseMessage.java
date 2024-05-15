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
    private Object object;
    @JsonProperty("successes")
    private Boolean successes;
    @JsonProperty("message")
    private String message;
    public ResponseMessage(Object object, Boolean successes, String message) {
        this.object = object;
        this.successes = successes;
        this.message = message;
    }
}