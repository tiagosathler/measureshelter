package com.agrotechfields.measureshelter.exception;

/**
 * The Class EntityNotFoundException.
 */
public class EntityNotFoundException extends Exception {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new entity not found exception.
   *
   * @param msg the msg
   */
  public EntityNotFoundException(String msg) {
    super(msg);
  }
}
