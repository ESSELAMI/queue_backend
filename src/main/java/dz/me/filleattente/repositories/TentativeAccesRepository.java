package dz.me.filleattente.repositories;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import dz.me.filleattente.entities.TentativeAcces;

/**
 *
 * @author Tarek Mekriche
 */
public interface TentativeAccesRepository extends JpaRepository<TentativeAcces, String> {

	@Query(nativeQuery = true, value = "select tentative from tentative_acces where ip_request = ?1")
	Integer getNombreTentative(String ip);

	/*
	 * @Query(nativeQuery = true, value =
	 * " select  EXTRACT (SECOND FROM (sysdate - (c.date_tentative)) DAY TO SECOND)\r\n"
	 * +
	 * "       + 60 * EXTRACT (MINUTE FROM (sysdate - c.date_tentative) DAY TO SECOND)\r\n"
	 * +
	 * "       + 60 * 60 * EXTRACT (HOUR FROM (sysdate - c.date_tentative) DAY TO SECOND)  total,c.tentative  tentative"
	 * + " from tentative_acces c  where c.ip_request = ?1 ")
	 * public List<Map<String, Object>> searchDelaisLastTentative(String ipRequest);
	 */

	@Query(nativeQuery = true, value = " SELECT TIME_TO_SEC(TIMEDIFF(NOW(), c.date_tentative))  total,c.tentative  tentative from tentative_acces c  where c.ip_request = ?1 ")
	public List<Map<String, Object>> searchDelaisLastTentative(String ipRequest);

	@Query(nativeQuery = true, value = " SELECT NOW()")
	public Date sysdate();

}
