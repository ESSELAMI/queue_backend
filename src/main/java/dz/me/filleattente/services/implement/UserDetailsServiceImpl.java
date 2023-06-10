package dz.me.filleattente.services.implement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import dz.me.filleattente.entities.Role;
import dz.me.filleattente.entities.User;
import dz.me.filleattente.repositories.UserRepository;

/**
 *
 * @author Tarek Mekriche
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username).get();

    Collection<GrantedAuthority> authorities = new ArrayList<>();
    List<Role> arr;

    arr = List.copyOf(user.getRoles());
    for (int i = 0; i < arr.size(); i++) {
      SimpleGrantedAuthority authority = new SimpleGrantedAuthority(arr.get(i).getName());
      authorities.add(authority);
    }

    Boolean enabled = true;
    try {

      enabled = user.isEnabled();
    } catch (Exception e) {
      enabled = false;
    }
    try {
      if (user.getPassword().equals(null)) {

      }
    } catch (Exception e) {
      user.setPassword("");
    }

    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), enabled, true,
        true, true, authorities);
  }

}
