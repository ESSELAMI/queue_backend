package dz.me.filleattente.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import dz.me.filleattente.entities.Role;

/**
 *
 * @author Tarek Mekriche
 */
public interface RoleRepository extends JpaRepository<Role, UUID> {

  @Query(value = "delete from Roles", nativeQuery = true)
  public void deleteAll();

  public Optional<Role> findByName(String string);
}
