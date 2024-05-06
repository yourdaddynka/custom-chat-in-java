package chat.modules.springsecurity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageModel {
    @JsonProperty(value = "message", required = true)
    private String message;
    @JsonProperty(value = "roomName", required = true)
    private String roomName;
    @JsonProperty(value = "senderName", required = true)
    private String senderName;
    @JsonProperty(value = "timestamp", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime timestamp;
}
