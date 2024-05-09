package org.letishal.springsecurityandauthenticator.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Getter
@ToString
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    Long id;

    @Column(unique = true, nullable = false)
    @Size(min = 4, max = 20, message = "Диапазон символов от 4 до 20")
    String userName;

    @Size(min = 8, message = "не меньше 8 символов")
    char[] userPassword;

    String token;

    String email;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "clients_roles",
            joinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    Set<Role> roles = new HashSet<>();

    public Client() {}


    public void addRole(Role role) {
        roles.add(role);
        role.getClients().add(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getClients().remove(this);
    }
}

