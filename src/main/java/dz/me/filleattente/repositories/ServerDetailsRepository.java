package dz.me.filleattente.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import dz.me.filleattente.entities.ServerDetails;

public interface ServerDetailsRepository extends JpaRepository<ServerDetails, UUID> {

}
