package org.letishal.springsecurityandauthenticator.service.impl;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.letishal.springsecurityandauthenticator.models.Client;
import org.letishal.springsecurityandauthenticator.repository.ClientRepository;
import org.letishal.springsecurityandauthenticator.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientRepositoryImpl implements ClientService<Client, Long> {
    @NonNull
    ClientRepository repository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public Optional<List<Client>> findAll() {
        return Optional.of(repository.findAll());
    }

    @Override
    public Optional<Client> create(Client entity) {
        if (repository.findByUserName(entity.getUserName()).isPresent()) {return Optional.empty();}
        return Optional.of(
                repository.save(
                        Client.builder()
                                .userName(entity.getUserName())
                                .email(entity.getEmail())
                                .token(entity.getToken())
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
        repository.deleteById(aLong);}

    @Override
    public Optional<Client> findByUserName(String userName){return repository.findByUserName(userName);}
}
