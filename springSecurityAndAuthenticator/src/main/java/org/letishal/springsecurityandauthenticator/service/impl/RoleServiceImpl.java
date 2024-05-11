package org.letishal.springsecurityandauthenticator.service.impl;

import org.letishal.springsecurityandauthenticator.models.Role;
import org.letishal.springsecurityandauthenticator.repository.RoleRepository;
import org.letishal.springsecurityandauthenticator.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService<Role, Long> {
     RoleRepository repository;

    @Override
    public Optional<List<Role>> findAll() {return Optional.of(repository.findAll());}

    @Override
    public Optional<Role> create(Role entity) {return Optional.of(repository.save(entity));}

    @Override
    public Optional<Role> update(Role entity) {
        return Optional.of(repository.save(entity));
    }

    @Override
    public void remove(Long aLong) {
        repository.findRoleById(aLong)
                .filter(e->!e.getName().equals("ADMIN"))
                .ifPresent(role -> repository.deleteById(aLong));
        }


    @Override
    public Optional<Role> findRoleById(Long id) {
        return repository.findRoleById(id);
    }

    @Override
    public Optional<Role> findByRoleName(String userName) {
        return repository.findByName(userName);
    }
}
