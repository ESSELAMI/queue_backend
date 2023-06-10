package dz.me.filleattente.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import dz.me.filleattente.entities.User;

public interface UserService {

    public Optional<User> findByUsername(String username);

    public Optional<User> findById(UUID idUser);

    public User save(User user);

    public User savePassword(User user);

    public void delete(UUID idUser);

    public List<User> findAll();

}
