package com.agrotechfields.measureshelter.dto;

import com.agrotechfields.measureshelter.domain.Role;
import com.agrotechfields.measureshelter.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * The Class UserDto.
 */
public class UserDto {

  /** The username. */
  @NotBlank
  @Size(min = 4, max = 10)
  @Pattern(regexp = "^(?!^[A-Z0-9]{10}$).*$", message = "must not be a isle serial number")
  private String username;

  /** The password. */
  @NotBlank
  @Size(min = 6, max = 14)
  private String password;

  /** The role. */
  private Role role;

  /**
   * Instantiates a new user dto.
   */
  public UserDto() {}

  /**
   * Instantiates a new user dto.
   *
   * @param username the username
   * @param password the password
   */
  public UserDto(String username, String password) {
    this.username = username;
    this.password = password;
  }

  /**
   * User from dto.
   *
   * @return the user
   */
  public User userFromDto() {
    return new User(null, username, password, role);
  }

  /**
   * Gets the username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username.
   *
   * @param username the new username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets the password.
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the password.
   *
   * @param password the new password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Gets the role.
   *
   * @return the role
   */
  public Role getRole() {
    return role;
  }

  /**
   * Sets the role.
   *
   * @param role the new role
   */
  public void setRole(Role role) {
    this.role = role;
  }
}
