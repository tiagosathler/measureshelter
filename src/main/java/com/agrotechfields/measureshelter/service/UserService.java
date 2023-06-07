package com.agrotechfields.measureshelter.service;

import com.agrotechfields.measureshelter.domain.User;
import com.agrotechfields.measureshelter.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The Class CustomUserDetailsService.
 */
@Service
public class UserService implements UserDetailsService {
  
  /** The user repository. */
  @Autowired
  private UserRepository userRepository;

  /**
   * Load user by username.
   *
   * @param username the username
   * @return the user details
   * @throws UsernameNotFoundException the username not found exception
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByUsername(username);

    if (user.isEmpty()) {
      throw new UsernameNotFoundException(username);
    }

    return user.get();
  }
}
