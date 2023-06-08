package com.agrotechfields.measureshelter.controller;

import com.agrotechfields.measureshelter.domain.Role;
import com.agrotechfields.measureshelter.domain.User;
import com.agrotechfields.measureshelter.dto.UserDto;
import com.agrotechfields.measureshelter.dto.UserResponseDto;
import com.agrotechfields.measureshelter.exception.EntityAlreadyExistsException;
import com.agrotechfields.measureshelter.exception.EntityNotFoundException;
import com.agrotechfields.measureshelter.exception.InvalidIdException;
import com.agrotechfields.measureshelter.exception.NotPermittedException;
import com.agrotechfields.measureshelter.service.UserService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * The Class UserController.
 */
@RestController
@RequestMapping("${user.endpoint}")
public class UserController {

  /** The endpoint. */
  @Value("${user.endpoint}")
  private String endpoint;

  /** The user service. */
  @Autowired
  private UserService userService;

  /**
   * Creates the user.
   *
   * @param userDto the user dto
   * @param isAdmin the is admin boolean parameter
   * @return the response entity
   * @throws EntityAlreadyExistsException the entity already exists exception
   */
  @PostMapping
  public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserDto userDto,
      @RequestParam(name = "isAdmin", defaultValue = "false", required = false) Boolean isAdmin)
      throws EntityAlreadyExistsException {
    User user = userService.createUser(userDto, isAdmin);
    return ResponseEntity.created(buildUri(user.getId().toString())).body(convertToDto(user));
  }

  /**
   * Register isle.
   *
   * @param id the id
   * @return the response entity
   * @throws EntityAlreadyExistsException the entity already exists exception
   * @throws EntityNotFoundException the entity not found exception
   */
  @PostMapping("/isle/{id}")
  public ResponseEntity<UserResponseDto> registerIsle(@PathVariable("id") String id)
      throws EntityAlreadyExistsException, EntityNotFoundException {
    User user = userService.registerIsleUser(id);
    return ResponseEntity.created(buildUri(user.getId().toString())).body(convertToDto(user));
  }

  /**
   * Find all users.
   *
   * @return the response entity
   */
  @GetMapping
  public ResponseEntity<List<UserResponseDto>> findAll() {
    List<User> users = userService.findAllUsers();
    return ResponseEntity.ok().body(convertToDto(users));
  }

  /**
   * Find user by id.
   *
   * @param id the id
   * @return the response entity
   * @throws EntityNotFoundException the entity not found exception
   * @throws InvalidIdException the invalid id exception
   */
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> findById(@PathVariable("id") String id)
      throws EntityNotFoundException, InvalidIdException {
    User user = userService.findUserById(id);
    return ResponseEntity.ok().body(convertToDto(user));
  }

  /**
   * Update context user.
   *
   * @param userDto the user dto
   * @return the response entity
   * @throws EntityAlreadyExistsException the entity already exists exception
   */
  @PutMapping
  public ResponseEntity<UserResponseDto> update(@RequestBody @Valid UserDto userDto)
      throws EntityAlreadyExistsException {
    User user = userService.updateContextUser(userDto);
    return ResponseEntity.ok().body(convertToDto(user));
  }

  /**
   * Toggle role by user id.
   *
   * @param id the id
   * @return the response entity
   * @throws InvalidIdException the invalid id exception
   * @throws NotPermittedException the not permitted exception
   * @throws EntityNotFoundException the entity not found exception
   */
  @PatchMapping("/{id}/toggle/role")
  public ResponseEntity<Map<String, String>> toggleRole(@PathVariable("id") String id)
      throws InvalidIdException, NotPermittedException, EntityNotFoundException {
    Role role = userService.toggleRoleById(id);
    Map<String, String> response = new HashMap<>();
    response.put("role", role.name());
    return ResponseEntity.ok().body(response);
  }

  /**
   * Toggle is enable by user id.
   *
   * @param id the id
   * @return the response entity
   * @throws InvalidIdException the invalid id exception
   * @throws NotPermittedException the not permitted exception
   * @throws EntityNotFoundException the entity not found exception
   */
  @PatchMapping("/{id}/toggle/enable")
  public ResponseEntity<Map<String, Boolean>> toggleEnable(@PathVariable("id") String id)
      throws InvalidIdException, NotPermittedException, EntityNotFoundException {
    boolean isEnable = userService.toggleIsEnable(id);
    Map<String, Boolean> response = new HashMap<>();
    response.put("isEnable", isEnable);
    return ResponseEntity.ok().body(response);
  }

  /**
   * Delete User by id.
   *
   * @param id the id
   * @return the response entity
   * @throws InvalidIdException the invalid id exception
   * @throws EntityNotFoundException the entity not found exception
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteById(@PathVariable("id") String id)
      throws InvalidIdException, EntityNotFoundException {
    userService.deleteUserById(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Builds the uri.
   *
   * @param id the id
   * @return the uri
   */
  private URI buildUri(String id) {
    return ServletUriComponentsBuilder.fromCurrentContextPath().path(endpoint + "/{id}")
        .buildAndExpand(id).toUri();
  }

  /**
   * Convert to dto.
   *
   * @param users the users
   * @return the list
   */
  private List<UserResponseDto> convertToDto(List<User> users) {
    return users.stream().map(UserResponseDto::new).toList();
  }

  /**
   * Convert to dto.
   *
   * @param user the user
   * @return the user response dto
   */
  private UserResponseDto convertToDto(User user) {
    return new UserResponseDto(user);
  }
}
