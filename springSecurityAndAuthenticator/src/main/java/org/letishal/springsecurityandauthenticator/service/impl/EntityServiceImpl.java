package org.letishal.springsecurityandauthenticator.service.impl;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.letishal.springsecurityandauthenticator.repository.EntityRepository;
import org.letishal.springsecurityandauthenticator.service.EntityService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EntityServiceImpl<T, ID> implements EntityService<T, ID> {
    @NonNull
    EntityRepository<T, ID> repository;

    @Override
    public Optional<List<T>> findAll() {
        return Optional.of(repository.findAll());
    }

    @Override
    public Optional<T> create(T entity) {
        return Optional.of(repository.save(entity));
    }

    @Override
    public Optional<T> update(T entity) {
        return Optional.of(repository.save(entity));
    }

    @Override
    public void remove(ID id) {
        repository.deleteById(id);
    }

}
