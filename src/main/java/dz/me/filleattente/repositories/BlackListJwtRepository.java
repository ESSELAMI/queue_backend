package dz.me.filleattente.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dz.me.filleattente.entities.BlackListRefreshToken;

/**
 *
 * @author Tarek Mekriche
 */
public interface BlackListJwtRepository extends JpaRepository<BlackListRefreshToken, String> {

}
