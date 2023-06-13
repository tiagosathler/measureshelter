package com.agrotechfields.measureshelter.dto.response;

import com.agrotechfields.measureshelter.domain.Measure;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The Class MeasureResponseDefaultDto.
 */
public class MeasureResponseDefaultDto implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The id. */
  private String id;

  /** The isle. */
  private IsleResponseShortDto isleResponseShortDto;

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

  /** The pressure (hPa). */
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
   * Instantiates a new measure response default dto.
   *
   * @param measure the measure
   */
  public MeasureResponseDefaultDto(Measure measure) {
    this.id = measure.getId();
    this.airTemp = measure.getAirTemp();
    this.gndTemp = measure.getGndTemp();
    this.windSpeed = measure.getWindSpeed();
    this.windDirection = measure.getWindDirection();
    this.irradiance = measure.getIrradiance();
    this.pressure = measure.getPressure();
    this.airHumidity = measure.getAirHumidity();
    this.gndHumidity = measure.getGndHumidity();
    this.precipitation = measure.getPrecipitation();
    this.rainIntensity = measure.getRainIntensity();
    this.timestamp = measure.getTimestamp();
    this.isleResponseShortDto = new IsleResponseShortDto(measure.getIsle());
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
   * Gets the isle.
   *
   * @return the isle
   */
  public IsleResponseShortDto getIsle() {
    return isleResponseShortDto;
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
   * Gets the gnd temp.
   *
   * @return the gnd temp
   */
  public BigDecimal getGndTemp() {
    return gndTemp;
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
   * Gets the wind direction.
   *
   * @return the wind direction
   */
  public BigDecimal getWindDirection() {
    return windDirection;
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
   * Gets the pressure.
   *
   * @return the pressure
   */
  public BigDecimal getPressure() {
    return pressure;
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
   * Gets the gnd humidity.
   *
   * @return the gnd humidity
   */
  public BigDecimal getGndHumidity() {
    return gndHumidity;
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
   * Gets the rain intensity.
   *
   * @return the rain intensity
   */
  public BigDecimal getRainIntensity() {
    return rainIntensity;
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
