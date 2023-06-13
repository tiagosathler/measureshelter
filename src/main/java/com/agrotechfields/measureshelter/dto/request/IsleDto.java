package com.agrotechfields.measureshelter.dto.request;

import com.agrotechfields.measureshelter.domain.Isle;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
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
  @NotEmpty
  @Pattern(regexp = "^[A-Z0-9]{10}$",
      message = "must be 10 digits including numbers and capital letters")
  private String serialNumber;

  /** The latitude (degrees). */
  @NotNull
  @DecimalMin(value = "-90", inclusive = false)
  @DecimalMax(value = "90", inclusive = false)
  private BigDecimal latitude;

  /** The longitude (degrees). */
  @NotNull
  @DecimalMin(value = "-180", inclusive = false)
  @DecimalMax(value = "180")
  private BigDecimal longitude;

  /** The altitude (meters). */
  @Positive
  private BigDecimal altitude;

  /** The is it working (boolean). */
  private Boolean isItWorking = true;

  /** The sampling interval (minutes). */
  @Min(1)
  @Max(3600)
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
   * @param latitude the latitude
   * @param longitude the longitude
   * @param altitude the altitude
   * @param isItWorking the is it working
   * @param samplingInterval the sampling interval
   */
  public IsleDto(String serialNumber, BigDecimal latitude, BigDecimal longitude,
      BigDecimal altitude, Boolean isItWorking, Integer samplingInterval) {
    this();
    this.serialNumber = serialNumber;
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
    return new Isle(id, serialNumber, latitude, longitude, altitude, isItWorking,
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

  /**
   * Gets the id.
   *
   * @return the id
   */
  public String getId() {
    return id;
  }
}
