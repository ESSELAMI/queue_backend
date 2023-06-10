package dz.me.filleattente.services.implement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dz.me.filleattente.entities.Role;
import dz.me.filleattente.exceptions.ResourceForbiddenException;
import dz.me.filleattente.repositories.RoleRepository;
import dz.me.filleattente.services.RoleService;

/**
 *
 * @author Tarek Mekriche
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Optional<Role> findById(UUID roleId) {
        return roleRepository.findById(roleId);
    }

    @Override
    public void deleteById(UUID roleId) {
        try {
            roleRepository.deleteById(roleId);
        } catch (Exception e) {
            throw new ResourceForbiddenException(e.getMessage());
        }
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

}
