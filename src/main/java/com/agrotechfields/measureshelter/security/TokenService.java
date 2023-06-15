package com.agrotechfields.measureshelter.security;

import com.agrotechfields.measureshelter.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * The Class TokenService.
 */
@Service
public class TokenService {
  /** The secret. */
  @Value("${security.token.secret:MySecretPhrase}")
  private String secret;

  /** The validity. */
  @Value("${security.token.validity.hours:24}")
  private String validity;

  /** The zone offset. */
  private static final String ZONE_OFFSET = "-03:00";

  /** The issuer. */
  private static final String ISSUER = "Agro_Techfields";

  /**
   * Encode token.
   *
   * @param user the user
   * @return the string
   * @throws JWTCreationException the JWT creation exception
   */
  public String encodeToken(User user) throws JWTCreationException {
    Algorithm algorithm = Algorithm.HMAC256(secret);
    System.out.println("########");
    System.out.println(secret);
    System.out.println(validity);
    System.out.println(user.getUsername());
    System.out.println(user.getPassword());
    System.out.println(ISSUER);
    System.out.println(defineIssuedAt());
    System.out.println(defineExpiresAt());
    System.out.println("########");
    return JWT
        .create()
        .withIssuer(ISSUER)
        .withIssuedAt(defineIssuedAt())
        .withSubject(user.getUsername())
        .withExpiresAt(defineExpiresAt())
        .sign(algorithm);
  }

  /**
   * Decode token.
   *
   * @param token the token
   * @return the string
   * @throws JWTDecodeException the JWT decode exception
   * @throws TokenExpiredException the token expired exception
   */
  public String decodeToken(String token)
      throws JWTDecodeException, TokenExpiredException, SignatureVerificationException {
    Algorithm algorithm = Algorithm.HMAC256(secret);
    return JWT
        .require(algorithm)
        .withIssuer(ISSUER)
        .build()
        .verify(token)
        .getSubject();
  }

  /**
   * Define expires at.
   *
   * @return the instant
   */
  private Instant defineExpiresAt() {
    return LocalDateTime
        .now()
        .plusHours(Integer.parseInt(validity))
        .toInstant(ZoneOffset.of(ZONE_OFFSET));
  }

  /**
   * Define issued at.
   *
   * @return the instant
   */
  private Instant defineIssuedAt() {
    return LocalDateTime
        .now()
        .toInstant(ZoneOffset.of(ZONE_OFFSET));
  }
}
