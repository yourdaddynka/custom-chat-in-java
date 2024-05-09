package org.letishal.springsecurityandauthenticator.service;

import org.letishal.springsecurityandauthenticator.models.Client;

import java.util.Optional;

public interface ClientService<T,ID> extends EntityService<T,ID>{
    Optional<T> findByUserName(String userName);
}
