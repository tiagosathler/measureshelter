package com.agrotechfields.measureshelter.dto;

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
