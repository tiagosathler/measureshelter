package com.agrotechfields.measureshelter.service;

import com.agrotechfields.measureshelter.domain.Isle;
import com.agrotechfields.measureshelter.domain.Role;
import com.agrotechfields.measureshelter.domain.User;
import com.agrotechfields.measureshelter.dto.UserDto;
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

  @Autowired
  private IsleRepository isleRepository;

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
    } else {
      userDto.setRole(Role.ROLE_USER);
    }

    userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

    User user = userDto.userFromDto();

    return userRepository.insert(user);
  }

  /**
   * Register isle user.
   *
   * @param isleId the isle id
   * @return the user
   * @throws EntityNotFoundException the entity not found exception
   * @throws EntityAlreadyExistsException the entity already exists exception
   */
  public User registerIsleUser(String isleId)
      throws EntityNotFoundException, EntityAlreadyExistsException {
    Optional<Isle> foundIsle = isleRepository.findById(isleId);

    if (foundIsle.isEmpty()) {
      throw new EntityNotFoundException("Isle");
    }
    Isle isle = foundIsle.get();

    Optional<User> foundUser = userRepository.findByUsername(isle.getSerialNumber());

    if (foundUser.isPresent()) {
      throw new EntityAlreadyExistsException("Isle user");
    }

    String encodedPassword = passwordEncoder.encode(isle.getPassword());

    User user = new User(null, isle.getSerialNumber(), encodedPassword, Role.ROLE_ISLE);
    return userRepository.insert(user);
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
   * @param id the id
   * @return the user
   * @throws EntityNotFoundException the entity not found exception
   * @throws InvalidIdException the invalid id exception
   */
  public User findUserById(String id) throws EntityNotFoundException, InvalidIdException {
    ObjectId objectId = getObjectId(id);
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
  public User updateContextUser(UserDto userDto)
      throws EntityAlreadyExistsException {
    Optional<User> foundUser = userRepository.findByUsername(userDto.getUsername());

    if (foundUser.isPresent()) {
      throw new EntityAlreadyExistsException("User");
    }

    User contextUser =
        (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    contextUser.setUsername(userDto.getUsername());
    contextUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
    return userRepository.save(contextUser);
  }

  /**
   * Toggle role by id.
   *
   * @param id the id
   * @return the role
   * @throws InvalidIdException the invalid id exception
   * @throws NotPermittedException the not permitted exception
   * @throws EntityNotFoundException the entity not found exception
   */
  public Role toggleRoleById(String id)
      throws InvalidIdException, NotPermittedException, EntityNotFoundException {
    ObjectId objectId = getObjectId(id);

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
   * @param id the id
   * @return true, if successful
   * @throws EntityNotFoundException the entity not found exception
   * @throws InvalidIdException the invalid id exception
   */
  public boolean toggleIsEnable(String id) throws EntityNotFoundException, InvalidIdException {
    ObjectId objectId = getObjectId(id);

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
   * @param id the id
   * @throws InvalidIdException the invalid id exception
   * @throws EntityNotFoundException the entity not found exception
   */
  public void deleteUserById(String id) throws InvalidIdException, EntityNotFoundException {
    ObjectId objectId = getObjectId(id);
    Optional<User> foundUser = userRepository.findById(objectId);
    if (foundUser.isEmpty()) {
      throw new EntityNotFoundException("User");
    }
    userRepository.delete(foundUser.get());
  }

  /**
   * Gets the object id.
   *
   * @param id the id
   * @return the object id
   * @throws InvalidIdException the invalid id exception
   */
  private ObjectId getObjectId(String id) throws InvalidIdException {
    try {
      return new ObjectId(id);
    } catch (IllegalArgumentException e) {
      throw new InvalidIdException(id);
    }
  }
}
