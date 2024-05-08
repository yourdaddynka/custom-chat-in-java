package chat.modules.springsecurity.model.test;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    private String sender;
    private LocalDateTime date;
}
