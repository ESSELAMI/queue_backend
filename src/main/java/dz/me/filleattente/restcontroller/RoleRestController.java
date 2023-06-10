package dz.me.filleattente.restcontroller;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import dz.me.filleattente.entities.Role;
import dz.me.filleattente.models.OperationModel;
import dz.me.filleattente.models.RoleModel;
import dz.me.filleattente.services.RoleService;
import dz.me.filleattente.utils.ResponseEntityUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 *
 * @author ABDELLATIF ESSELAMI
 */
@RestController
@RequestMapping(path = "/api/v1/roles", produces = "application/json;charset=UTF-8")
@SecurityRequirement(name = "bearerAuth")
public class RoleRestController {
    @Autowired
    RoleService roleService;

    @GetMapping("/{role-id}")
    public ResponseEntity<?> findById(@PathVariable(name = "role-id") String idRole) {
        Optional<Role> role = roleService.findById(UUID.fromString(idRole));
        if (!role.isPresent()) {
            return ResponseEntityUtils.ExceptionResponseEntity(idRole + " role not found",
                    HttpStatus.FORBIDDEN.value());
        }
        return ResponseEntity.ok(role.get());
    }

    @DeleteMapping("/{role-id}")
    public ResponseEntity<?> delete(@PathVariable(name = "role-id") String idRole) {
        try {
            roleService.deleteById(UUID.fromString(idRole));
            OperationModel operationModel = new OperationModel("success");
            return ResponseEntity.ok(operationModel);
        } catch (Exception e) {
            return ResponseEntityUtils.ExceptionResponseEntity(e.getMessage(),
                    HttpStatus.FORBIDDEN.value());
        }

    }

    @PostMapping("/ajouter-role")
    public ResponseEntity<?> save(@RequestBody RoleModel roleModel) {
        Role role = new Role();
        role.setName(roleModel.getRole());

        return ResponseEntity.ok(roleService.save(role));
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {

        return ResponseEntity.ok(roleService.findAll());
    }

}
