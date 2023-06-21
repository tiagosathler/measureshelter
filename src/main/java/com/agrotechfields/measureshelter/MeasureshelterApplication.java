package com.agrotechfields.measureshelter;

import com.agrotechfields.measureshelter.domain.Role;
import com.agrotechfields.measureshelter.domain.User;
import com.agrotechfields.measureshelter.repository.UserRepository;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The Class MeasureshelterApplication.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.agrotechfields.measureshelter"})
public class MeasureshelterApplication implements CommandLineRunner {

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    Locale.setDefault(Locale.US);
    SpringApplication.run(MeasureshelterApplication.class, args);
  }

  /**
   * Run inserting root user.
   *
   * @param args the args
   * @throws Exception the exception
   */
  @Override
  public void run(String... args) throws Exception {
    final String username = "root";
    final String password = "123456";

    Optional<User> foundUser = userRepository.findByUsername(username);
    if (foundUser.isEmpty()) {
      String encodedPassword = passwordEncoder.encode(password);
      userRepository.insert(new User(null, username, encodedPassword, Role.ROLE_ADMIN));
    }
  }
}
