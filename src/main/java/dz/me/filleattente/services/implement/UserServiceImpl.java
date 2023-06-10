package dz.me.filleattente.services.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import dz.me.filleattente.entities.User;
import dz.me.filleattente.exceptions.ResourceForbiddenException;
import dz.me.filleattente.repositories.UserRepository;
import dz.me.filleattente.services.UserService;

/**
 *
 * @author Tarek Mekriche
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(UUID idUser) {
        return userRepository.findById(idUser);

    }

    @Override
    public User save(User user) {

        return userRepository.save(user);
    }

    @Override
    public User savePassword(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public void delete(UUID idUser) {
        try {
            userRepository.deleteById(idUser);
        } catch (Exception e) {
            throw new ResourceForbiddenException(e.getMessage());
        }

    }

    @Override
    public List<User> findAll() {
        List<User> l = userRepository.findAll1();
        if (l.size() != 0)
            return userRepository.findAll1();
        else
            return new ArrayList<User>();
    }

}
