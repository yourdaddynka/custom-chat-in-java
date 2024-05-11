package org.letishal.springsecurityandauthenticator.service;

import org.letishal.springsecurityandauthenticator.models.Client;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EntityService<T,ID> {
    Optional<List<T>> findAll();
    Optional<T> create(T entity);
    Optional<T> update(T entity);
    void remove(ID id);
}
