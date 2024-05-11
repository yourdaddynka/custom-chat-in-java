package org.letishal.springsecurityandauthenticator.service;


import org.letishal.springsecurityandauthenticator.models.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RoleService<Role,Long>{
    Optional<List<Role>> findAll();
    Optional<Role> create(Role entity);
    Optional<Role> update(Role entity);
    void remove(Long Long);
    Optional<Role> findByRoleName(String userName);
    Optional<Role> findRoleById(Long Long);
}
