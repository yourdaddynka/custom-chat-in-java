package org.letishal.springsecurityandauthenticator.service.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.letishal.springsecurityandauthenticator.models.Client;
import org.letishal.springsecurityandauthenticator.repository.ClientRepository;
import org.letishal.springsecurityandauthenticator.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientServiceImpl implements ClientService<Client, Long> ,UserDetailsService {
    @Autowired
    ClientRepository repository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public Optional<Client> findByUserName(String userName) {
        return repository.findByClientUsername(userName);
    }

    @Override
    public Optional<List<Client>> findAll() {
        return Optional.of(repository.findAll());
    }

    @Override
    public Optional<Client> create(Client entity) {
        if (repository.findByClientUsername(entity.getClientUsername()).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(
                repository.save(
                        Client.builder()
                                .clientUsername(entity.getClientUsername())
                                .email(entity.getEmail())
                                .roles(entity.getRoles())
                                .userPassword(passwordEncoder.encode((Arrays.toString(entity.getUserPassword()))).toCharArray())
                                .build()
                )
        );
    }

    @Override
    public Optional<Client> update(Client entity) {
        return Optional.of(repository.save(entity));//возможно требуется обновление токена
    }

    @Override
    public void remove(Long aLong) {
        repository.findById(aLong).ifPresent(
                e -> {
                    if (e.getRoles().stream().noneMatch(role -> role.getName().equals("ADMIN"))) {
                        repository.deleteById(aLong);
                    } else {
                        System.out.println("Попытка ужалить пользоватедя");
                    }
                }
        );
    }// возможно,стоит ограничить в правах удаление пользователя(нельзя удалить админа

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client user = findByUserName(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }
}