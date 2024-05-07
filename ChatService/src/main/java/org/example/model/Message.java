package org.example.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "message")
@Getter
@Setter
public class Message {

    @Id
    private String id;
    private String content;
    private String sender;
    @DBRef
    private Room room;

    public Message() {
    }

    public Message(String content, String sender, Room room) {
        this.content = content;
        this.sender = sender;
        this.room = room;
    }
}