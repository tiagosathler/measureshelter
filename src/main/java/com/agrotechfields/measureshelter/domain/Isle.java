package com.agrotechfields.measureshelter.domain;

import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The Class Isle.
 */
@Document(collection = "isles")

public class Isle {

  /** The id. */
  @Id
  private String id;

  /** The serial number. */
  private String serialNumber;

  /** The password. */
  private String password;

  /** The latidude. */
  private String latidude;

  /** The longitude. */
  private String longitude;

  /** The altitude. */
  private Double altitude;

  /** The is it working. */
  private Boolean isItWorking;

  /** The sampling interval. */
  private Integer samplingInterval;

  /**
   * Instantiates a new isle.
   */
  public Isle() {}

  /**
   * Instantiates a new isle.
   *
   * @param id the id
   * @param serialNumber the serial number
   * @param password the password
   * @param latidude the latidude
   * @param longitude the longitude
   * @param altitude the altitude
   * @param isItWorking the is it working
   * @param samplingInterval the sampling interval
   */
  public Isle(String id, String serialNumber, String password, String latidude, String longitude,
      Double altitude, Boolean isItWorking, Integer samplingInterval) {
    this.id = id;
    this.serialNumber = serialNumber;
    this.password = password;
    this.latidude = latidude;
    this.longitude = longitude;
    this.altitude = altitude;
    this.isItWorking = isItWorking;
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

  /**
   * Sets the id.
   *
   * @param id the new id
   */
  public void setId(String id) {
    this.id = id;
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
   * Gets the latidude.
   *
   * @return the latidude
   */
  public String getLatidude() {
    return latidude;
  }

  /**
   * Sets the latidude.
   *
   * @param latidude the new latidude
   */
  public void setLatidude(String latidude) {
    this.latidude = latidude;
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
   * Hash code.
   *
   * @return the int
   */
  @Override
  public int hashCode() {
    return Objects.hash(serialNumber);
  }

  /**
   * Equals.
   *
   * @param obj the obj
   * @return true, if successful
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Isle)) {
      return false;
    }
    Isle other = (Isle) obj;
    return Objects.equals(serialNumber, other.serialNumber);
  }


}
