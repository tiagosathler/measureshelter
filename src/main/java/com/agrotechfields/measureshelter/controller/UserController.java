package com.agrotechfields.measureshelter.controller;

import com.agrotechfields.measureshelter.domain.Role;
import com.agrotechfields.measureshelter.domain.User;
import com.agrotechfields.measureshelter.dto.request.IsleUserDto;
import com.agrotechfields.measureshelter.dto.request.UserDto;
import com.agrotechfields.measureshelter.dto.response.UserResponseDto;
import com.agrotechfields.measureshelter.exception.EntityAlreadyExistsException;
import com.agrotechfields.measureshelter.exception.EntityNotFoundException;
import com.agrotechfields.measureshelter.exception.InvalidIdException;
import com.agrotechfields.measureshelter.exception.NotPermittedException;
import com.agrotechfields.measureshelter.service.IdService;
import com.agrotechfields.measureshelter.service.UserService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
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
@RequestMapping("${endpoint.user}")
public class UserController {

  /** The endpoint. */
  @Value("${endpoint.user}")
  private String endpoint;

  /** The user service. */
  @Autowired
  private UserService userService;

  /** The id service. */
  @Autowired
  private IdService idService;

  /**
   * Creates the user.
   *
   * @param userDto the user dto
   * @param isAdmin the is admin boolean parameter
   * @param isSat the is satellite boolean parameter
   * @return the response entity with new user
   * @throws EntityAlreadyExistsException the entity already exists exception
   */
  @PostMapping
  public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserDto userDto,
      @RequestParam(name = "isAdmin", defaultValue = "false", required = false) Boolean isAdmin,
      @RequestParam(name = "isSat", defaultValue = "false", required = false) Boolean isSat)
      throws EntityAlreadyExistsException {
    User user = userService.createUser(userDto, isAdmin, isSat);
    return ResponseEntity.created(buildUri(user.getId().toString())).body(convertToDto(user));
  }

  /**
   * Register isle.
   *
   * @param registerIsleDto the register isle dto
   * @return the response entity with new user (isle user)
   * @throws EntityAlreadyExistsException the entity already exists exception
   * @throws EntityNotFoundException the entity not found exception
   */
  @PostMapping("/isle")
  public ResponseEntity<UserResponseDto> registerIsle(
      @RequestBody @Valid IsleUserDto registerIsleDto)
      throws EntityAlreadyExistsException, EntityNotFoundException {
    User user = userService.registerIsleUser(registerIsleDto);
    return ResponseEntity.created(buildUri(user.getId().toString())).body(convertToDto(user));
  }

  /**
   * Find all users.
   *
   * @return the response entity with found users
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
   * @return the response entity with found user
   * @throws EntityNotFoundException the entity not found exception
   * @throws InvalidIdException the invalid id exception
   */
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> findById(@PathVariable("id") String id)
      throws EntityNotFoundException, InvalidIdException {
    ObjectId objectId = idService.getObjectId(id);
    User user = userService.findUserById(objectId);
    return ResponseEntity.ok().body(convertToDto(user));
  }

  /**
   * Update context user.
   *
   * @param userDto the user dto
   * @return the response entity with updated user
   * @throws EntityAlreadyExistsException the entity already exists exception
   */
  @PutMapping
  public ResponseEntity<UserResponseDto> update(@RequestBody @Valid UserDto userDto)
      throws EntityAlreadyExistsException {
    User user = userService.updateContextUser(userDto);
    return ResponseEntity.ok().body(convertToDto(user));
  }

  /**
   * Update isle user.
   *
   * @param registerIsleDto the register isle dto
   * @return the response entity with updated user (isle user)
   * @throws EntityNotFoundException the entity not found exception
   */
  @PutMapping("/isle")
  public ResponseEntity<UserResponseDto> updateIsleUser(
      @RequestBody @Valid IsleUserDto registerIsleDto) throws EntityNotFoundException {
    User user = userService.updateIsleUser(registerIsleDto);
    return ResponseEntity.ok().body(convertToDto(user));
  }

  /**
   * Toggle role by user id.
   *
   * @param id the id
   * @return the response entity with update role
   * @throws InvalidIdException the invalid id exception
   * @throws NotPermittedException the not permitted exception
   * @throws EntityNotFoundException the entity not found exception
   */
  @PatchMapping("/{id}/toggle/role")
  public ResponseEntity<Map<String, String>> toggleRole(@PathVariable("id") String id)
      throws InvalidIdException, NotPermittedException, EntityNotFoundException {
    ObjectId objectId = idService.getObjectId(id);
    Role role = userService.toggleRoleById(objectId);
    Map<String, String> response = new HashMap<>();
    response.put("role", role.name());
    return ResponseEntity.ok().body(response);
  }

  /**
   * Toggle is enable by user id.
   *
   * @param id the id
   * @return the response entity with updated enable
   * @throws InvalidIdException the invalid id exception
   * @throws NotPermittedException the not permitted exception
   * @throws EntityNotFoundException the entity not found exception
   */
  @PatchMapping("/{id}/toggle/enable")
  public ResponseEntity<Map<String, Boolean>> toggleEnable(@PathVariable("id") String id)
      throws InvalidIdException, NotPermittedException, EntityNotFoundException {
    ObjectId objectId = idService.getObjectId(id);
    boolean isEnable = userService.toggleIsEnable(objectId);
    Map<String, Boolean> response = new HashMap<>();
    response.put("isEnable", isEnable);
    return ResponseEntity.ok().body(response);
  }

  /**
   * Delete User by id.
   *
   * @param id the id
   * @return the response entity without content
   * @throws InvalidIdException the invalid id exception
   * @throws EntityNotFoundException the entity not found exception
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteById(@PathVariable("id") String id)
      throws InvalidIdException, EntityNotFoundException {
    ObjectId objectId = idService.getObjectId(id);
    userService.deleteUserById(objectId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Builds the uri.
   *
   * @param id the id
   * @return the uri
   */
  private URI buildUri(String id) {
    return ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path(endpoint + "/{id}")
        .buildAndExpand(id)
        .toUri();
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
