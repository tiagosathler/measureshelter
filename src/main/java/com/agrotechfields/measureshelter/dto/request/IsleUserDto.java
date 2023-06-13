package com.agrotechfields.measureshelter.dto.request;

import com.agrotechfields.measureshelter.domain.Role;
import com.agrotechfields.measureshelter.domain.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * The Class RegisterIsleDto.
 */
public class IsleUserDto {

  /** The serial number. */
  @NotEmpty
  @Pattern(regexp = "^[A-Z0-9]{10}$",
      message = "must be 10 digits including numbers and capital letters")
  private String serialNumber;

  /** The password. */
  @NotEmpty @Size(min = 5, max = 14)
  private String password;
  
  /** The role. */
  private Role role;

  /**
   * Instantiates a new register isle dto.
   */
  public IsleUserDto() {
    this.role = Role.ROLE_ISLE;
  }

  /**
   * Instantiates a new register isle dto.
   *
   * @param serialNumber the serial number
   * @param password the password
   */
  public IsleUserDto(String serialNumber, String password) {
    this();
    this.serialNumber = serialNumber;
    this.password = password;
  }
  
  /**
   * User from dto.
   *
   * @return the user
   */
  public User userFromDto() {
    return new User(null, serialNumber, password, role);
  }

  /**
   * Gets the serial number.
   *
   * @return the serial number
   */
  public String getSerialNumber() {
    return serialNumber;
  }

  /**
   * Sets the serial number.
   *
   * @param serialNumber the new serial number
   */
  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
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
}
