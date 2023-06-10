package dz.me.filleattente.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Tarek Mekriche
 */
@Data
@AllArgsConstructor
public class JwtResponse {
  private String token;
  private String refreshJWT;
  private String type = "Bearer";
  // private Long id;
  private String username;
  private String email;
  // private List<String> roles;

  public JwtResponse(String accessToken, String refreshJWT, String username, String email) {
    this.token = accessToken;
    this.refreshJWT = refreshJWT;
    this.username = username;
    this.email = email;
  }
}
