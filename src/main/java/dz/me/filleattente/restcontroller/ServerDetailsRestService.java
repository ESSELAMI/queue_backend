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
import dz.me.filleattente.entities.ServerDetails;
import dz.me.filleattente.models.OperationModel;
import dz.me.filleattente.models.RoleModel;
import dz.me.filleattente.services.RoleService;
import dz.me.filleattente.services.ServerDetailsService;
import dz.me.filleattente.utils.ResponseEntityUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(path = "/api/v1/server-details", produces = "application/json;charset=UTF-8")
@SecurityRequirement(name = "bearerAuth")
public class ServerDetailsRestService {
    @Autowired
    private ServerDetailsService serverDetailsService;

    @GetMapping("/all")
    public ResponseEntity<?> findById() {
        return ResponseEntity.ok(serverDetailsService.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> save(@RequestBody ServerDetails serverDetails) {
        ;
        return ResponseEntity.ok(serverDetailsService.add(serverDetails));
    }
}
