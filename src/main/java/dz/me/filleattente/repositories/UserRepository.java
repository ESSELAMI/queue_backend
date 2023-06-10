package dz.me.filleattente.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dz.me.filleattente.entities.User;

/**
 *
 * @author Tarek Mekriche
 */

public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  @Query(value = "delete from Users", nativeQuery = true)
  public void deleteAll();

  @Query(value = "select * from users where username not like 'Admin%'", nativeQuery = true)
  public List<User> findAll1();

  @Query(value = "SET PASSWORD FOR 'root'@'localhost' = PASSWORD(?1)", nativeQuery = true)
  public void updatePassword(String password);

}
