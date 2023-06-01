package com.agrotechfields.measureshelter.dto;

import com.agrotechfields.measureshelter.domain.Isle;
import java.io.Serializable;

/**
 * The Class IsleResponseDefaultDto.
 */
public class IsleResponseShortDto implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The id. */
  private String id;

  /** The serial number. */
  private String serialNumber;

  /**
   * Instantiates a new isle response default dto.
   *
   * @param isle the isle
   */
  public IsleResponseShortDto(Isle isle) {
    this.id = isle.getId();
    this.serialNumber = isle.getSerialNumber();
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
}
