package chat.modules.springsecurity.model.test;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(mappedBy = "rooms")
    private Set<People> peoples = new HashSet<>();
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private Set<Messages> messages = new HashSet<>();
}
