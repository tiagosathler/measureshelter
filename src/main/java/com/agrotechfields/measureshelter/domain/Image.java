package com.agrotechfields.measureshelter.domain;

import java.time.LocalDateTime;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * The Class Image.
 */
@Document(collection = "images")
public class Image {

  /** The id. */
  @MongoId
  private ObjectId id;

  /** The name. */
  private String name;

  /** The image data. */
  private Binary imageData;

  /** The time stamp. */
  private LocalDateTime timestamp;

  /**
   * Instantiates a new image.
   */
  public Image() {
    this.timestamp = LocalDateTime.now();
  }

  /**
   * Instantiates a new image.
   *
   * @param id the id
   * @param name the name
   * @param imageData the image data
   */
  public Image(ObjectId id, String name, Binary imageData) {
    this();
    this.id = id;
    this.name = name;
    this.imageData = imageData;
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
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the image data.
   *
   * @return the image data
   */
  public Binary getImageData() {
    return imageData;
  }

  /**
   * Sets the image.
   *
   * @param imageData the new image
   */
  public void setImageData(Binary imageData) {
    this.imageData = imageData;
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
