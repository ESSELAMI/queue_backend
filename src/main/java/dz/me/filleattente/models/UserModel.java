package dz.me.filleattente.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 *
 * @author Tarek Mekriche
 */
@Getter
public class UserModel {

    private String username;
    private String email;
    private String password;
    private String lastName;
    private String firstName;
    @JsonProperty("accountNonExpired")
    private boolean isAccountNonExpired;
    @JsonProperty("accountNonLocked")
    private boolean isAccountNonLocked;
    @JsonProperty("credentialsNonExpired")
    private boolean isCredentialsNonExpired;
    @JsonProperty("enabled")
    private boolean isEnabled;
    private String serviceId;
}
