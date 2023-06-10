package dz.me.filleattente.restcontroller;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import dz.me.filleattente.entities.Role;
import dz.me.filleattente.entities.User;
import dz.me.filleattente.filters.JwtUtils;
import dz.me.filleattente.models.OperationModel;
import dz.me.filleattente.models.UpdatePasswordModel;
import dz.me.filleattente.models.UserModel;
import dz.me.filleattente.services.RoleService;
import dz.me.filleattente.services.UserService;
import dz.me.filleattente.utils.ResponseEntityUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 *
 * @author ABDELLATIF ESSELAMI
 */
@RestController
@RequestMapping(path = "/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
public class UserRestController {
    @Autowired
    private UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/username/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable(name = "username") String username) {
        Optional<User> user = userService.findByUsername(username);
        if (!user.isPresent()) {
            return ResponseEntityUtils.ExceptionResponseEntity(username + " user not found",
                    HttpStatus.FORBIDDEN.value());
        }
        return ResponseEntity.ok(user.get());
    }

    @GetMapping(path = "/by-token", produces = { "application/json; charset=UTF-8" })
    public ResponseEntity<?> findByToken(HttpServletRequest request) {
        String idUser = jwtUtils.ClaimAsString(request, "user-id");
        Optional<User> user = userService.findById(UUID.fromString(idUser));
        if (!user.isPresent()) {
            return ResponseEntityUtils.ExceptionResponseEntity(idUser + " user-id not found",
                    HttpStatus.FORBIDDEN.value());
        }
        return ResponseEntity.ok(user.get());
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<?> findById(@PathVariable(name = "user-id") String idUser) {
        Optional<User> user = userService.findById(UUID.fromString(idUser));
        if (!user.isPresent()) {
            return ResponseEntityUtils.ExceptionResponseEntity(idUser + " user not found",
                    HttpStatus.FORBIDDEN.value());
        }
        return ResponseEntity.ok(user.get());
    }

    @GetMapping("/user/{user-id}/delete/role/{role-id}")
    public ResponseEntity<?> affecterRole(@PathVariable(name = "user-id") String idUser,
            @PathVariable(name = "role-id") String idRole) {
        System.out.println(idUser);
        System.out.println(idRole);
        Optional<User> user = userService.findById(UUID.fromString(idUser));
        Optional<Role> role = roleService.findById(UUID.fromString(idRole));

        if (!user.isPresent()) {
            return ResponseEntityUtils.ExceptionResponseEntity(idUser + " user not found",
                    HttpStatus.FORBIDDEN.value());
        }
        if (!role.isPresent()) {
            return ResponseEntityUtils.ExceptionResponseEntity(idUser + " role not found",
                    HttpStatus.FORBIDDEN.value());
        }
        user.get().getRoles().remove(role.get());
        return ResponseEntity.ok(userService.save(user.get()));
    }

    @PostMapping("/ajouter-user")
    public ResponseEntity<?> save(@RequestBody() UserModel userModel) {
        User user = new User();
        user.setUsername(userModel.getUsername());
        user.setLastname(userModel.getLastName());
        user.setFirstname(userModel.getFirstName());
        user.setEmail(userModel.getEmail());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setAccountNonExpired(userModel.isAccountNonExpired());
        user.setAccountNonLocked(userModel.isAccountNonLocked());
        user.setCredentialsNonExpired(userModel.isCredentialsNonExpired());
        user.setEnabled(userModel.isEnabled());
        user.setRoles(new HashSet<Role>());
        return ResponseEntity.ok(userService.save(user));
    }

    @GetMapping("affecter/user/{user-id}/service/{service-id}")
    public ResponseEntity<?> affecterUserToService(@PathVariable(name = "user-id") String idUser,
            @PathVariable(name = "service-id") String idService) {
        Optional<User> user = userService.findById(UUID.fromString(idUser));

        if (!user.isPresent()) {
            return ResponseEntityUtils.ExceptionResponseEntity(idUser + " user not found",
                    HttpStatus.FORBIDDEN.value());
        }
        userService.save(user.get());
        return ResponseEntity.ok(user.get());
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(HttpServletRequest request,
            @RequestBody() UpdatePasswordModel updatePasswordModel) {
        String idUser = jwtUtils.ClaimAsString(request, "user-id");
        Optional<User> user = userService.findById(UUID.fromString(idUser));
        if (!user.isPresent()) {
            return ResponseEntityUtils.ExceptionResponseEntity(idUser + " user-id not found",
                    HttpStatus.FORBIDDEN.value());
        }
        Boolean b = BCrypt.checkpw(updatePasswordModel.getOldPassword(), user.get().getPassword());
        if (!b) {
            return ResponseEntityUtils.ExceptionResponseEntity(" inccorect password",
                    HttpStatus.FORBIDDEN.value());
        } else {
            user.get().setPassword(updatePasswordModel.getNewPassword());
            return ResponseEntity.ok(userService.savePassword(user.get()));
        }

    }

    @DeleteMapping("/{user-id}")
    public ResponseEntity<?> delete(@PathVariable(name = "user-id") String idUser) {
        try {
            userService.delete(UUID.fromString(idUser));
            OperationModel operationModel = new OperationModel("success");
            return ResponseEntity.ok(operationModel);
        } catch (Exception e) {
            return ResponseEntityUtils.ExceptionResponseEntity(e.getMessage(),
                    HttpStatus.FORBIDDEN.value());
        }

    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {

        return ResponseEntity
                .ok(userService.findAll());

    }

    @GetMapping("/affecter-role-user/user/{user-id}/role/{role-id}")
    public ResponseEntity<?> AffecterRoleToUser(@PathVariable(name = "user-id") String idUser,
            @PathVariable(name = "role-id") String idRole) {
        Optional<User> user = userService.findById(UUID.fromString(idUser));
        Optional<Role> role = roleService.findById(UUID.fromString(idRole));
        if ((!user.isPresent()) || (!role.isPresent())) {
            return ResponseEntityUtils.ExceptionResponseEntity("user-id or role-id not found",
                    HttpStatus.NOT_FOUND.value());
        }
        user.get().getRoles().add(role.get());

        return ResponseEntity.ok(userService.save(user.get()));
    }

}
