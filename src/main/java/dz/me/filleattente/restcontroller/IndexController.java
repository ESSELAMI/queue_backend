package dz.me.filleattente.restcontroller;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.me.filleattente.entities.Role;
import dz.me.filleattente.entities.User;
import dz.me.filleattente.repositories.RoleRepository;
import dz.me.filleattente.repositories.UserRepository;

@RestController
public class IndexController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	// ALTER DATABASE file_attente CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
	// ALTER TABLE structure CONVERT TO CHARACTER SET utf8mb4 COLLATE
	// utf8mb4_unicode_ci;
	// ;ALTER TABLE service CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci

	@GetMapping(value = "/xx")
	public String indexOperation() {

		Role role = new Role("ADMIN");

		roleRepository.save(role);

		User user = new User();
		user.setEmail("Tarek@gmail.com");
		user.setFirstname("Tarek");
		user.setLastname("Mekriche");
		user.setPassword(
				"$2a$12$TrDJwYYx3ivqkPYaDFNojOkQJ/Naiqj5Q66tah3NdW7FjqUMGCDm.");
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		user.setRoles(roles);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setEnabled(true);
		user.setUsername("tarek");
		user.setEmail("Tarek@gmail.com");
		User user1 = new User();
		user1.setFirstname("Abdelatif");
		user1.setLastname("Esselami");
		user1.setPassword(
				"$2a$12$TrDJwYYx3ivqkPYaDFNojOkQJ/Naiqj5Q66tah3NdW7FjqUMGCDm.");
		roles.add(role);
		user1.setRoles(roles);
		user1.setAccountNonExpired(true);
		user1.setAccountNonLocked(true);
		user1.setCredentialsNonExpired(true);
		user1.setEnabled(true);
		user1.setUsername("abdelatif");
		user1.setEmail("abdelatif@gmail.com");

		User user2 = new User();
		user2.setFirstname("Abdelhak");
		user2.setLastname("Omerani");
		user2.setPassword(
				"$2a$12$TrDJwYYx3ivqkPYaDFNojOkQJ/Naiqj5Q66tah3NdW7FjqUMGCDm.");
		roles.add(role);
		user2.setRoles(roles);
		user2.setAccountNonExpired(true);
		user2.setAccountNonLocked(true);
		user2.setCredentialsNonExpired(true);
		user2.setEnabled(true);
		user2.setUsername("Abdelhak");
		user1.setEmail("Abdelhak@gmail.com");
		userRepository.save(user);
		userRepository.save(user1);
		userRepository.save(user2);

		return "redirect:/swagger-ui.html";

	}
}
