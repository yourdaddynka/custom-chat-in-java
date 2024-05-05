package org.example.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "user_chat")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "token", nullable = false)
    private String token;

    public User() {
    }

    public User(String login, String password, String role, String token) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.token = token;
    }
}
