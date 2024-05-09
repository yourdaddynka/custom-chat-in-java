package org.letishal.springsecurityandauthenticator.repository;

import org.letishal.springsecurityandauthenticator.models.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends EntityRepository<Role,Long> {
    Optional<Role> findRoleById(Long id);
    Optional<Role> findByName(String userName);
}
