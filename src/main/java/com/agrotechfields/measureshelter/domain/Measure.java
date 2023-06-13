package com.agrotechfields.measureshelter.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * The Class Measure. REF:
 * https://wp.ufpel.edu.br/agrometeorologia/informacoes/instrumentos-meteorologicos/
 */
@Document
public class Measure {

  /** The id. */
  @MongoId
  private ObjectId id;

  /** The isle id. */
  private ObjectId isleId;

  /** The air temperature (°C). */
  private BigDecimal airTemp;

  /** The underground temperature (°C). */
  private BigDecimal gndTemp;

  /** The wind speed (m/s). */
  private BigDecimal windSpeed;

  /** The wind direction (°). */
  private BigDecimal windDirection;

  /** The irradiance (Wh/m¹). */
  private BigDecimal irradiance;

  /** The pressure (mPa). */
  private BigDecimal pressure;

  /** The humidity (%). */
  private BigDecimal airHumidity;

  /** The underground humidity (%). */
  private BigDecimal gndHumidity;

  /** The precipitation (mm). */
  private BigDecimal precipitation;

  /** The rain intensity (mm/h). */
  private BigDecimal rainIntensity;

  /** The timestamp. */
  private LocalDateTime timestamp;

  /**
   * Instantiates a new measure.
   */
  public Measure() {}

  /**
   * Instantiates a new measure.
   *
   * @param id the object id
   * @param isleId the isle object id
   * @param airTemp the air temp
   * @param gndTemp the gnd temp
   * @param windSpeed the wind speed
   * @param windDirection the wind direction
   * @param irradiance the irradiance
   * @param pressure the pressure
   * @param airHumidity the air humidity
   * @param gndHumidity the gnd humidity
   * @param precipitation the precipitation
   * @param rainIntensity the rain intensity
   * @param timestamp the timestamp
   */
  public Measure(ObjectId id, ObjectId isleId, BigDecimal airTemp, BigDecimal gndTemp,
      BigDecimal windSpeed, BigDecimal windDirection, BigDecimal irradiance, BigDecimal pressure,
      BigDecimal airHumidity, BigDecimal gndHumidity, BigDecimal precipitation,
      BigDecimal rainIntensity, LocalDateTime timestamp) {
    this.id = id;
    this.isleId = isleId;
    this.airTemp = airTemp;
    this.gndTemp = gndTemp;
    this.windSpeed = windSpeed;
    this.windDirection = windDirection;
    this.irradiance = irradiance;
    this.pressure = pressure;
    this.airHumidity = airHumidity;
    this.gndHumidity = gndHumidity;
    this.precipitation = precipitation;
    this.rainIntensity = rainIntensity;
    this.timestamp = timestamp;
  }

  /**
   * Gets the id.
   *
   * @return the id
   */
  public ObjectId getId() {
    return id;
  }

  /**
   * Sets the id.
   *
   * @param id the new id
   */
  public void setId(ObjectId id) {
    this.id = id;
  }

  /**
   * Gets the isle.
   *
   * @return the isle
   */
  public ObjectId getIsleId() {
    return isleId;
  }

  /**
   * Sets the isle.
   *
   * @param isleId the new isle
   */
  public void setIsleId(ObjectId isleId) {
    this.isleId = isleId;
  }

  /**
   * Gets the air temp.
   *
   * @return the air temp
   */
  public BigDecimal getAirTemp() {
    return airTemp;
  }

  /**
   * Sets the air temp.
   *
   * @param airTemp the new air temp
   */
  public void setAirTemp(BigDecimal airTemp) {
    this.airTemp = airTemp;
  }

  /**
   * Gets the gnd temp.
   *
   * @return the gnd temp
   */
  public BigDecimal getGndTemp() {
    return gndTemp;
  }

  /**
   * Sets the gnd temp.
   *
   * @param gndTemp the new gnd temp
   */
  public void setGndTemp(BigDecimal gndTemp) {
    this.gndTemp = gndTemp;
  }

  /**
   * Gets the wind speed.
   *
   * @return the wind speed
   */
  public BigDecimal getWindSpeed() {
    return windSpeed;
  }

  /**
   * Sets the wind speed.
   *
   * @param windSpeed the new wind speed
   */
  public void setWindSpeed(BigDecimal windSpeed) {
    this.windSpeed = windSpeed;
  }

  /**
   * Gets the wind direction.
   *
   * @return the wind direction
   */
  public BigDecimal getWindDirection() {
    return windDirection;
  }

  /**
   * Sets the wind direction.
   *
   * @param windDirection the new wind direction
   */
  public void setWindDirection(BigDecimal windDirection) {
    this.windDirection = windDirection;
  }

  /**
   * Gets the irradiance.
   *
   * @return the irradiance
   */
  public BigDecimal getIrradiance() {
    return irradiance;
  }

  /**
   * Sets the irradiance.
   *
   * @param irradiance the new irradiance
   */
  public void setIrradiance(BigDecimal irradiance) {
    this.irradiance = irradiance;
  }

  /**
   * Gets the pressure.
   *
   * @return the pressure
   */
  public BigDecimal getPressure() {
    return pressure;
  }

  /**
   * Sets the pressure.
   *
   * @param pressure the new pressure
   */
  public void setPressure(BigDecimal pressure) {
    this.pressure = pressure;
  }

  /**
   * Gets the air humidity.
   *
   * @return the air humidity
   */
  public BigDecimal getAirHumidity() {
    return airHumidity;
  }

  /**
   * Sets the air humidity.
   *
   * @param airHumidity the new air humidity
   */
  public void setAirHumidity(BigDecimal airHumidity) {
    this.airHumidity = airHumidity;
  }

  /**
   * Gets the gnd humidity.
   *
   * @return the gnd humidity
   */
  public BigDecimal getGndHumidity() {
    return gndHumidity;
  }

  /**
   * Sets the gnd humidity.
   *
   * @param gndHumidity the new gnd humidity
   */
  public void setGndHumidity(BigDecimal gndHumidity) {
    this.gndHumidity = gndHumidity;
  }

  /**
   * Gets the precipitation.
   *
   * @return the precipitation
   */
  public BigDecimal getPrecipitation() {
    return precipitation;
  }

  /**
   * Sets the precipitation.
   *
   * @param precipitation the new precipitation
   */
  public void setPrecipitation(BigDecimal precipitation) {
    this.precipitation = precipitation;
  }

  /**
   * Gets the rain intensity.
   *
   * @return the rain intensity
   */
  public BigDecimal getRainIntensity() {
    return rainIntensity;
  }

  /**
   * Sets the rain intensity.
   *
   * @param rainIntensity the new rain intensity
   */
  public void setRainIntensity(BigDecimal rainIntensity) {
    this.rainIntensity = rainIntensity;
  }

  /**
   * Gets the timestamp.
   *
   * @return the timestamp
   */
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  /**
   * Sets the timestamp.
   *
   * @param timestamp the new timestamp
   */
  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Hash code.
   *
   * @return the int
   */
  @Override
  public int hashCode() {
    return Objects.hash(id);
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
    if (!(obj instanceof Measure)) {
      return false;
    }
    Measure other = (Measure) obj;
    return Objects.equals(id, other.id);
  }
}
