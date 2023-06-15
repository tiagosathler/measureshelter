package com.agrotechfields.measureshelter.dto.response;

import java.io.Serializable;

/**
 * The Class TokenReponseDto.
 */
public class TokenReponseDto implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The token. */
  private String token;

  /**
   * Instantiates a new token reponse dto.
   */
  public TokenReponseDto() {}

  /**
   * Instantiates a new token reponse dto.
   *
   * @param token the token
   */
  public TokenReponseDto(String token) {
    this.token = token;
  }

  //  /**
  //   * Encode.
  //   *
  //   * @param token the token
  //   * @return the string
  //   */
  //  private String encode(String token) {
  //    String[] arr = token.split("\\.");
  //    String leftHead = arr[0].substring(0, 10);
  //    String rightHead = arr[0].substring(10, arr[0].length());
  //    String payload = arr[1];
  //    String signature = arr[2];
  //    return String.join(" joke ", leftHead, rightHead, payload, signature);
  //  }

  /**
   * Gets the token.
   *
   * @return the token
   */
  public String getToken() {
    return token;
  }

  /**
   * Sets the token.
   *
   * @param token the new token
   */
  public void setToken(String token) {
    this.token = token;
  }
}
