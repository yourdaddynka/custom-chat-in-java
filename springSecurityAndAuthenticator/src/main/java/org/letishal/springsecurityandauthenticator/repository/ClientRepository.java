package org.letishal.springsecurityandauthenticator.repository;

import org.letishal.springsecurityandauthenticator.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends EntityRepository<Client,Long> {
    Optional<Client> findByUserName(String userName);
}
