package org.letishal.springsecurityandauthenticator.service;


import org.letishal.springsecurityandauthenticator.models.Role;

import java.util.Optional;

public interface RoleService<T,ID> extends EntityService<T,ID>{
    Optional<T> findRoleById(Long id);
    Optional<T> findByRoleName(String userName);
}
