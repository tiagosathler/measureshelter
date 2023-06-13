package com.agrotechfields.measureshelter.dto.request;

import com.agrotechfields.measureshelter.domain.Measure;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.bson.types.ObjectId;

/**
 * The Class MeasureDto.
 */
public class MeasureDto implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The isle id. */
  private ObjectId isleId;

  /** The air temperature (°C). */
  @NotNull @Min(-20) @Max(50)
  private BigDecimal airTemp;

  /** The underground temperature (°C). */
  @NotNull @Min(-30) @Max(60)
  private BigDecimal gndTemp;

  /** The wind speed (m/s). */
  @NotNull @Min(0) @Max(30)
  private BigDecimal windSpeed;

  /** The wind direction (°). */
  @NotNull @Min(0) @DecimalMax(value = "360", inclusive = false)
  private BigDecimal windDirection;

  /** The irradiance (Wh/m¹). */
  @NotNull @Min(0) @Max(1500)
  private BigDecimal irradiance;

  /** The pressure (hPa). */
  @NotNull @Min(100) @Max(1200)
  private BigDecimal pressure;

  /** The humidity (%). */
  @NotNull @Min(0) @Max(100)
  private BigDecimal airHumidity;

  /** The underground humidity (%). */
  @NotNull @Min(0) @Max(100)
  private BigDecimal gndHumidity;

  /** The precipitation (mm). */
  @NotNull @Min(0) @Max(1000)
  private BigDecimal precipitation;

  /** The rain intensity (mm/h). */
  @NotNull @Min(0) @Max(1000)
  private BigDecimal rainIntensity;

  /** The timestamp. */
  private LocalDateTime timestamp;

  /**
   * Instantiates a new measure.
   */
  public MeasureDto() {
    this.timestamp = LocalDateTime.now();
  }

  /**
   * Instantiates a new measure.
   *
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
   */
  public MeasureDto(BigDecimal airTemp, BigDecimal gndTemp, BigDecimal windSpeed,
      BigDecimal windDirection, BigDecimal irradiance, BigDecimal pressure, BigDecimal airHumidity,
      BigDecimal gndHumidity, BigDecimal precipitation, BigDecimal rainIntensity) {
    this();
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
  }

  /**
   * Measure from dto.
   *
   * @return the measure
   */
  public Measure measureFromDto() {
    Measure measure = new Measure();
    measure.setAirTemp(airTemp);
    measure.setGndTemp(gndTemp);
    measure.setWindSpeed(windSpeed);
    measure.setWindDirection(windDirection);
    measure.setIrradiance(irradiance);
    measure.setPressure(pressure);
    measure.setAirHumidity(airHumidity);
    measure.setGndHumidity(gndHumidity);
    measure.setPrecipitation(precipitation);
    measure.setRainIntensity(rainIntensity);
    measure.setTimestamp(timestamp);
    return measure;
  }

  /**
   * Gets the isle id.
   *
   * @return the isle id
   */
  public ObjectId getIsleId() {
    return isleId;
  }

  /**
   * Sets the isle id.
   *
   * @param isleId the new isle id
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
}
