package org.letishal.springsecurityandauthenticator.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Getter
@ToString
@Builder
@AllArgsConstructor
public class Client implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    Long id;

    @Column(unique = true, nullable = false)
    @Size(min = 4, max = 20, message = "Диапазон символов от 4 до 20")
    String clientUsername;

    @Size(min = 8, message = "не меньше 8 символов")
    @Column(nullable = false)
    char[] userPassword;


    @Column(nullable = false)
    String email;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "clients_roles",
            joinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    Set<Role> roles = new HashSet<>();

    public Client() {
    }


    public void addRole(Role role) {
        roles.add(role);
        role.getClients().add(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getClients().remove(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles();
    }

    @Override
    public String getPassword() {
        return String.valueOf(this.getUserPassword());
    }

    @Override
    public String getUsername() {
        return this.getClientUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

