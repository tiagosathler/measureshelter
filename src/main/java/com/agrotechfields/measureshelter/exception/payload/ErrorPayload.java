package com.agrotechfields.measureshelter.exception.payload;

import java.io.Serializable;
import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;

/**
 * The Class ErrorPayload.
 */
public class ErrorPayload implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The timestamp. */
  private String timestamp;

  /** The status. */
  private Integer status;

  /** The error. */
  private String error;

  /** The message. */
  private String message;

  /**
   * Instantiates a new error payload.
   *
   * @param message the message
   * @param httpStatus the http status
   */
  public ErrorPayload(String message, HttpStatus httpStatus) {
    this.timestamp = ZonedDateTime.now().toString();
    this.status = httpStatus.value();
    this.error = httpStatus.name();
    this.message = message;
  }

  /**
   * Gets the status.
   *
   * @return the status
   */
  public Integer getStatus() {
    return status;
  }

  /**
   * Sets the status.
   *
   * @param status the new status
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * Gets the error.
   *
   * @return the error
   */
  public String getError() {
    return error;
  }

  /**
   * Sets the error.
   *
   * @param error the new error
   */
  public void setError(String error) {
    this.error = error;
  }

  /**
   * Gets the message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the message.
   *
   * @param message the new message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Gets the timestamp.
   *
   * @return the timestamp
   */
  public String getTimestamp() {
    return timestamp;
  }
}
