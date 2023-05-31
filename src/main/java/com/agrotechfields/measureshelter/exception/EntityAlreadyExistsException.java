package com.agrotechfields.measureshelter.exception;

/**
 * The Class EntityAlreadyExistsException.
 */
public class EntityAlreadyExistsException extends Exception {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new entity already exists exception.
   *
   * @param msg the msg
   */
  public EntityAlreadyExistsException(String msg) {
    super(msg);
  }
}
