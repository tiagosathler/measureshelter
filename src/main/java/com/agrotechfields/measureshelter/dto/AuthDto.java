package com.agrotechfields.measureshelter.dto;

import jakarta.validation.constraints.NotEmpty;

/**
 * The Class AuthDto.
 */
public class AuthDto {
  
  /** The username. */
  @NotEmpty
  private String username;
  
  /** The password. */
  @NotEmpty
  private String password;

  /**
   * Instantiates a new auth dto.
   */
  public AuthDto() {}

  /**
   * Instantiates a new auth dto.
   *
   * @param username the username
   * @param password the password
   */
  public AuthDto(String username, String password) {
    this.username = username;
    this.password = password;
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
}
