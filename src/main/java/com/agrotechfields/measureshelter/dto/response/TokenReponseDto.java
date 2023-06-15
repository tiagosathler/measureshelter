package com.agrotechfields.measureshelter.dto.response;

import java.io.Serializable;

/**
 * The Class TokenReponseDto.
 */
public class TokenReponseDto implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The right head. */
  private String rightHead;

  /** The left head. */
  private String leftHead;

  /** The payload. */
  private String payload;

  /** The signature. */
  private String signature;

  /**
   * Instantiates a new token reponse dto.
   */
  public TokenReponseDto() {}

  /**
   * Instantiates a new token reponse dto.
   *
   * @param rightHead the right head
   * @param leftHead the left head
   * @param payload the payload
   * @param signature the signature
   */
  public TokenReponseDto(String rightHead, String leftHead, String payload, String signature) {
    this.leftHead = leftHead;
    this.rightHead = rightHead;
    this.payload = payload;
    this.signature = signature;
  }

  /**
   * Gets the right head.
   *
   * @return the right head
   */
  public String getRightHead() {
    return rightHead;
  }

  /**
   * Sets the right head.
   *
   * @param rightHead the new right head
   */
  public void setRightHead(String rightHead) {
    this.rightHead = rightHead;
  }

  /**
   * Gets the left head.
   *
   * @return the left head
   */
  public String getLeftHead() {
    return leftHead;
  }

  /**
   * Sets the left head.
   *
   * @param leftHead the new left head
   */
  public void setLeftHead(String leftHead) {
    this.leftHead = leftHead;
  }

  /**
   * Gets the payload.
   *
   * @return the payload
   */
  public String getPayload() {
    return payload;
  }

  /**
   * Sets the payload.
   *
   * @param payload the new payload
   */
  public void setPayload(String payload) {
    this.payload = payload;
  }


  /**
   * Gets the signature.
   *
   * @return the signature
   */
  public String getSignature() {
    return signature;
  }

  /**
   * Sets the signature.
   *
   * @param signature the new signature
   */
  public void setSignature(String signature) {
    this.signature = signature;
  }
}
