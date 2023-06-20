package com.agrotechfields.measureshelter.dto.response;

import com.agrotechfields.measureshelter.domain.Image;
import java.time.LocalDateTime;

/**
 * The Class ImageResponseDto.
 */
public class ImageResponseDto {

  /** The id. */
  private String id;
  
  /** The name. */
  private String name;

  /** The timestamp. */
  private LocalDateTime timestamp;

  /**
   * Instantiates a new image response dto.
   *
   * @param image the image
   */
  public ImageResponseDto(Image image) {
    this.id = image.getId().toHexString();
    this.name = image.getName();
    this.timestamp = image.getTimestamp();
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
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
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
