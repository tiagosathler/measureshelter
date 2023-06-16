package com.agrotechfields.measureshelter.service;

import com.agrotechfields.measureshelter.domain.Isle;
import com.agrotechfields.measureshelter.domain.Role;
import com.agrotechfields.measureshelter.domain.User;
import com.agrotechfields.measureshelter.dto.request.IsleUserDto;
import com.agrotechfields.measureshelter.dto.request.UserDto;
import com.agrotechfields.measureshelter.exception.DivergentSerialNumberException;
import com.agrotechfields.measureshelter.exception.EntityAlreadyExistsException;
import com.agrotechfields.measureshelter.exception.EntityNotFoundException;
import com.agrotechfields.measureshelter.exception.InvalidIdException;
import com.agrotechfields.measureshelter.exception.NotPermittedException;
import com.agrotechfields.measureshelter.repository.IsleRepository;
import com.agrotechfields.measureshelter.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The Class CustomUserDetailsService.
 */
@Service
public class UserService implements UserDetailsService {

  /** The user repository. */
  @Autowired
  private UserRepository userRepository;

  /** The isle repository. */
  @Autowired
  private IsleRepository isleRepository;

  /** The password encoder. */
  @Autowired
  private PasswordEncoder passwordEncoder;

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

  /**
   * Creates the user.
   *
   * @param userDto the user dto
   * @param isAdmin the is admin boolean
   * @return the user
   * @throws EntityAlreadyExistsException the entity already exists exception
   */
  public User createUser(UserDto userDto, boolean isAdmin) throws EntityAlreadyExistsException {
    Optional<User> foundUser = userRepository.findByUsername(userDto.getUsername());
    if (foundUser.isPresent()) {
      throw new EntityAlreadyExistsException("User");
    }

    if (isAdmin) {
      userDto.setRole(Role.ROLE_ADMIN);
    }

    String encodedPassword = passwordEncoder.encode(userDto.getPassword());
    userDto.setPassword(encodedPassword);

    return userRepository.insert(userDto.userFromDto());
  }

  /**
   * Register isle user.
   *
   * @param isleUserDto the register isle dto
   * @return the user
   * @throws EntityNotFoundException the entity not found exception
   * @throws EntityAlreadyExistsException the entity already exists exception
   * @throws DivergentSerialNumberException the divergent serial number exception
   */
  public User registerIsleUser(IsleUserDto isleUserDto)
      throws EntityNotFoundException, EntityAlreadyExistsException, DivergentSerialNumberException {
    Optional<Isle> foundIsle = isleRepository.findBySerialNumber(isleUserDto.getSerialNumber());

    if (foundIsle.isEmpty()) {
      throw new EntityNotFoundException("Isle");
    }

    Optional<User> foundUser = userRepository.findByUsername(isleUserDto.getSerialNumber());

    if (foundUser.isPresent()) {
      throw new EntityAlreadyExistsException(
          "Isle user by the serial number '" + isleUserDto.getSerialNumber() + "'");
    }

    String encodedPassword = passwordEncoder.encode(isleUserDto.getPassword());
    isleUserDto.setPassword(encodedPassword);

    return userRepository.insert(isleUserDto.userFromDto());
  }

  /**
   * Find all users.
   *
   * @return the list
   */
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  /**
   * Find user by id.
   *
   * @param objectId the ObjectId
   * @return the user
   * @throws EntityNotFoundException the entity not found exception
   * @throws InvalidIdException the invalid id exception
   */
  public User findUserById(ObjectId objectId) throws EntityNotFoundException, InvalidIdException {
    Optional<User> foundUser = userRepository.findById(objectId);
    if (foundUser.isEmpty()) {
      throw new EntityNotFoundException("User");
    }
    return foundUser.get();
  }

  /**
   * Update context user.
   *
   * @param userDto the user dto
   * @return the user
   * @throws EntityAlreadyExistsException the entity already exists exception
   */
  public User updateContextUser(UserDto userDto) throws EntityAlreadyExistsException {
    User contextUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Optional<User> foundUser = userRepository.findByUsername(userDto.getUsername());

    if (foundUser.isPresent() && !foundUser.get().getUsername().equals(contextUser.getUsername())) {
      throw new EntityAlreadyExistsException("User");
    }

    contextUser.setUsername(userDto.getUsername());
    contextUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
    return userRepository.save(contextUser);
  }

  /**
   * Update isle user password.
   *
   * @param isleUserDto the register isle dto
   * @return the user
   * @throws EntityNotFoundException the entity not found exception
   */
  public User updateIsleUser(IsleUserDto isleUserDto) throws EntityNotFoundException {
    Optional<Isle> foundIsle = isleRepository.findBySerialNumber(isleUserDto.getSerialNumber());
    if (foundIsle.isEmpty()) {
      throw new EntityNotFoundException("Isle");
    }

    Optional<User> foundUser = userRepository.findByUsername(isleUserDto.getSerialNumber());
    if (foundUser.isEmpty()) {
      throw new EntityNotFoundException("Register this isle first. Isle as user");
    }

    User user = foundUser.get();
    String encodedPassword = passwordEncoder.encode(isleUserDto.getPassword());
    user.setPassword(encodedPassword);
    return userRepository.save(user);
  }

  /**
   * Toggle role by id.
   *
   * @param objectId the ObjectId
   * @return the role
   * @throws InvalidIdException the invalid id exception
   * @throws NotPermittedException the not permitted exception
   * @throws EntityNotFoundException the entity not found exception
   */
  public Role toggleRoleById(ObjectId objectId)
      throws InvalidIdException, NotPermittedException, EntityNotFoundException {
    Optional<User> foundUser = userRepository.findById(objectId);
    if (foundUser.isEmpty()) {
      throw new EntityNotFoundException("User");
    }

    User user = foundUser.get();
    if (user.getRole().equals(Role.ROLE_ISLE)) {
      throw new NotPermittedException(
          "User '" + user.getUsername() + "' with role " + Role.ROLE_ISLE.name());
    }

    if (user.getRole().equals(Role.ROLE_USER)) {
      user.setRole(Role.ROLE_ADMIN);
    } else {
      user.setRole(Role.ROLE_USER);
    }

    userRepository.save(user);
    return user.getRole();
  }

  /**
   * Toggle is enable property.
   *
   * @param objectId the ObjectId
   * @return true, if successful
   * @throws EntityNotFoundException the entity not found exception
   * @throws InvalidIdException the invalid id exception
   */
  public boolean toggleIsEnable(ObjectId objectId)
      throws EntityNotFoundException, InvalidIdException {
    Optional<User> foundUser = userRepository.findById(objectId);
    if (foundUser.isEmpty()) {
      throw new EntityNotFoundException("User");
    }
    User user = foundUser.get();

    user.setEnabled(!user.isEnabled());
    userRepository.save(user);
    return user.isEnabled();
  }

  /**
   * Delete user by id.
   *
   * @param objectId the ObjectId
   * @throws InvalidIdException the invalid id exception
   * @throws EntityNotFoundException the entity not found exception
   */
  public void deleteUserById(ObjectId objectId) throws InvalidIdException, EntityNotFoundException {
    Optional<User> foundUser = userRepository.findById(objectId);
    if (foundUser.isEmpty()) {
      throw new EntityNotFoundException("User");
    }
    userRepository.delete(foundUser.get());
  }
}
