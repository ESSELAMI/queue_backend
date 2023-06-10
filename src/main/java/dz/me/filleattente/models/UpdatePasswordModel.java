package dz.me.filleattente.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Tarek Mekriche
 */
@Data
@Getter
@Setter
public class UpdatePasswordModel {

    private String oldPassword;
    private String newPassword;
}
