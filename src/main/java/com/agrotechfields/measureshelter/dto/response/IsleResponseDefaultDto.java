package com.agrotechfields.measureshelter.dto.response;

import com.agrotechfields.measureshelter.domain.Isle;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The Class IsleResponseDefaultDto.
 */
public class IsleResponseDefaultDto implements Serializable {

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
   * Instantiates a new isle response default dto.
   *
   * @param isle the isle
   */
  public IsleResponseDefaultDto(Isle isle) {
    this.id = isle.getId();
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
   * Gets the serial number.
   *
   * @return the serial number
   */
  public String getSerialNumber() {
    return serialNumber;
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
   * Gets the longitude.
   *
   * @return the longitude
   */
  public BigDecimal getLongitude() {
    return longitude;
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
   * Gets the checks if is it working.
   *
   * @return the checks if is it working
   */
  public Boolean getIsItWorking() {
    return isItWorking;
  }

  /**
   * Gets the sampling interval.
   *
   * @return the sampling interval
   */
  public Integer getSamplingInterval() {
    return samplingInterval;
  }
}
