package com.agrotechfields.measureshelter.dto.response;

import com.agrotechfields.measureshelter.domain.Isle;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The Class IsleResponseDefaultDto.
 */
public class IsleResponseDto implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The id. */
  private String id;

  /** The serial number. */
  private String serialNumber;

  /** The latitude. */
  private BigDecimal latitude;

  /** The longitude. */
  private BigDecimal longitude;

  /** The altitude. */
  private BigDecimal altitude;

  /** The is it working. */
  private Boolean isItWorking;

  /** The sampling interval. */
  private Integer samplingInterval;

  /**
   * Instantiates a new isle response dto.
   */
  public IsleResponseDto() {}

  /**
   * Instantiates a new isle response default dto.
   *
   * @param isle the isle
   */
  public IsleResponseDto(Isle isle) {
    this.id = isle.getId().toHexString();
    this.serialNumber = isle.getSerialNumber();
    this.latitude = isle.getLatitude();
    this.longitude = isle.getLongitude();
    this.altitude = isle.getAltitude();
    this.isItWorking = isle.getIsItWorking();
    this.samplingInterval = isle.getSamplingInterval();
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
   * Gets the latitude.
   *
   * @return the latitude
   */
  public BigDecimal getLatitude() {
    return latitude;
  }

  /**
   * Sets the latitude.
   *
   * @param latitude the new latitude
   */
  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }


  /**
   * Gets the longitude.
   *
   * @return the longitude
   */
  public BigDecimal getLongitude() {
    return longitude;
  }

  /**
   * Sets the longitude.
   *
   * @param longitude the new longitude
   */
  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  /**
   * Gets the altitude.
   *
   * @return the altitude
   */
  public BigDecimal getAltitude() {
    return altitude;
  }

  /**
   * Sets the altitude.
   *
   * @param altitude the new altitude
   */
  public void setAltitude(BigDecimal altitude) {
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
}
