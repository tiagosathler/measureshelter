package com.agrotechfields.measureshelter.dto.request;

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

  /**
   * Instantiates a new register isle dto.
   */
  public IsleUserDto() {}

  /**
   * Instantiates a new register isle dto.
   *
   * @param serialNumber the serial number
   * @param password the password
   */
  public IsleUserDto(String serialNumber, String password) {
    this.serialNumber = serialNumber;
    this.password = password;
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
}
