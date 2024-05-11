package org.letishal.springsecurityandauthenticator.service;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ClientService<T,ID> extends EntityService<T,ID>{
    Optional<T> findByUserName(String userName);}
