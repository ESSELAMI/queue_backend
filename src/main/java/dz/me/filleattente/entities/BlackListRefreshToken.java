package dz.me.filleattente.entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ABDELLATIF ESSELAMI
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonPropertyOrder({ "token", "username", "expired", "generatedOn", "generatedFromIp" })
public class BlackListRefreshToken {

	@Id
	private String token;
	private Date blackListedDate;
	private String blockedByAccesToken;
	private String blockedByUsername;
	private String username;
	private Date expired;
	private Date generatedOn;
	private String generatedFromIp;

}
