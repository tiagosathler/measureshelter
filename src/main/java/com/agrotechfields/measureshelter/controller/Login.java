package com.agrotechfields.measureshelter.controller;

import com.agrotechfields.measureshelter.domain.User;
import com.agrotechfields.measureshelter.dto.request.AuthDto;
import com.agrotechfields.measureshelter.dto.response.TokenReponseDto;
import com.agrotechfields.measureshelter.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class Login.
 */
@RestController
@RequestMapping("/login")
public class Login {

  /** The authentication manager. */
  @Autowired
  private AuthenticationManager authenticationManager;

  /** The token service. */
  @Autowired
  private TokenService tokenService;

  /**
   * Login.
   *
   * @param authDto the auth dto
   * @return the response entity
   */
  @PostMapping
  public ResponseEntity<TokenReponseDto> login(@RequestBody @Valid AuthDto authDto) {
    String username = authDto.getUsername();
    String password = authDto.getPassword();

    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(username, password);

    Authentication authentication = authenticationManager.authenticate(authToken);

    String token = tokenService.encodeToken((User) authentication.getPrincipal());
    System.out.println("------> token: " + token + " <------");

    return ResponseEntity.ok().body(new TokenReponseDto(token));
  }
}
