package com.agrotechfields.measureshelter.dto;

import com.agrotechfields.measureshelter.domain.Isle;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import org.bson.codecs.ObjectIdGenerator;

/**
 * The Class IsleDto.
 */
public class IsleDto implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The id. */
  private String id;

  /** The serial number. */
  @NotNull(message = "cannot be null")
  @Pattern(regexp = "^[A-Z0-9]{10}$",
      message = "must be 10 digits including numbers and capital letters")
  private String serialNumber;

  /** The password. */
  @NotNull(message = "cannot be null")
  @NotBlank(message = "cannot be empty")
  private String password;

  /**
   * The latitude. Regex from
   * https://gist.github.com/DebkanchanSamadder/1eb07af7d9595256535c5c71ea79d66e
   */
  @NotNull(message = "cannot be null")
  @Pattern(regexp = "^-?([0-8]?[0-9]|90)(\\.[0-9]{1,10})?$",
      message = "must be between -90.0 to 90.0")
  private String latitude;

  /**
   * The longitude. Regex from
   * https://gist.github.com/DebkanchanSamadder/1eb07af7d9595256535c5c71ea79d66e
   */
  @NotNull(message = "cannot be null")
  @Pattern(regexp = "^-?([0-9]{1,2}|1[0-7][0-9]|180)(\\.[0-9]{1,10})$",
      message = "must be between -180.0 to 180.0")
  private String longitude;

  /** The altitude. */
  @Positive(message = "must be positive")
  private Double altitude;

  /** The is it working. */
  private Boolean isItWorking = true;

  /** The sampling interval. */
  @Min(value = 1, message = "must be between 1 to 3600 minutes")
  @Max(value = 3600, message = "must be between 1 to 3600 minutes")
  private Integer samplingInterval = 5;

  /**
   * Instantiates a new isle dto.
   */
  public IsleDto() {
    this.id = new ObjectIdGenerator().generate().toString();
  }

  /**
   * Instantiates a new isle dto.
   *
   * @param serialNumber the serial number
   * @param password the password
   * @param latitude the latitude
   * @param longitude the longitude
   * @param altitude the altitude
   * @param isItWorking the is it working
   * @param samplingInterval the sampling interval
   */
  public IsleDto(String serialNumber, String password, String latitude, String longitude,
      Double altitude, Boolean isItWorking, Integer samplingInterval) {
    this();
    this.serialNumber = serialNumber;
    this.password = password;
    this.latitude = latitude;
    this.longitude = longitude;
    this.altitude = altitude;
    this.samplingInterval = samplingInterval;
    this.isItWorking = isItWorking;
  }

  /**
   * Isle from dto.
   *
   * @return the isle
   */
  public Isle isleFromDto() {
    return new Isle(id, serialNumber, password, latitude, longitude, altitude, isItWorking,
        samplingInterval);
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
   * Gets the latitude.
   *
   * @return the latitude
   */
  public String getLatitude() {
    return latitude;
  }

  /**
   * Sets the latitude.
   *
   * @param latitude the new latitude
   */
  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  /**
   * Gets the longitude.
   *
   * @return the longitude
   */
  public String getLongitude() {
    return longitude;
  }

  /**
   * Sets the longitude.
   *
   * @param longitude the new longitude
   */
  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  /**
   * Gets the altitude.
   *
   * @return the altitude
   */
  public Double getAltitude() {
    return altitude;
  }

  /**
   * Sets the altitude.
   *
   * @param altitude the new altitude
   */
  public void setAltitude(Double altitude) {
    this.altitude = altitude;
  }

  /**
   * Gets the checks if is it working.
   *
   * @return the checks if is it working
   */
  public Boolean getIsItWorking() {
    return isItWorking;
  }

  /**
   * Sets the checks if is it working.
   *
   * @param isItWorking the new checks if is it working
   */
  public void setIsItWorking(Boolean isItWorking) {
    this.isItWorking = isItWorking;
  }

  /**
   * Gets the sampling interval.
   *
   * @return the sampling interval
   */
  public Integer getSamplingInterval() {
    return samplingInterval;
  }

  /**
   * Sets the sampling interval.
   *
   * @param samplingInterval the new sampling interval
   */
  public void setSamplingInterval(Integer samplingInterval) {
    this.samplingInterval = samplingInterval;
  }

  /**
   * Gets the id.
   *
   * @return the id
   */
  public String getId() {
    return id;
  }
}
