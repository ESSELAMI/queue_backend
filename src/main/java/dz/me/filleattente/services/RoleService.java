package dz.me.filleattente.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import dz.me.filleattente.entities.Role;

public interface RoleService {
    public Optional<Role> findById(UUID roleId);

    public void deleteById(UUID roleId);

    public Role save(Role role);

    public List<Role> findAll();
}
