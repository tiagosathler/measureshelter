package com.agrotechfields.measureshelter.security;

import com.agrotechfields.measureshelter.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * The Class JwtSecurityFilter.
 */
@Component
public class JwtSecurityFilter extends OncePerRequestFilter {

  /** The token service. */
  @Autowired
  private TokenService tokenService;

  /** The user service. */
  @Autowired
  private UserService userService;

  /** The password encoder. */
  @Autowired
  private PasswordEncoder passwordEncoder;

  /** The handler exception resolver. */
  @Autowired
  private HandlerExceptionResolver handlerExceptionResolver;

  /**
   * Do filter internal.
   * Ref: https://stackoverflow.com/questions/34595605/how-to-manage-exceptions-thrown-in-filters-in-spring
   *
   * @param request the request
   * @param response the response
   * @param filterChain the filter chain
   * @throws ServletException the servlet exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      String token = getToken(request);

      if (token != null) {
        String username = tokenService.decodeToken(token);
        UserDetails user = userService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      handlerExceptionResolver.resolveException(request, response, null, e);
    }
  }

  /**
   * Gets the token.
   *
   * @param request the request
   * @return the token
   */
  private String getToken(HttpServletRequest request) {
    String authorizationHeader = request.getHeader("Authorization");
    System.out.println("====> Authorization: " + authorizationHeader);
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
      String tokenEncoded = authorizationHeader.replace("Bearer ", "");
      String[] arr = tokenEncoded.split(" joke ");

      String head = String.join("", arr[0], arr[1]);
      String payload = arr[2];
      String signature = arr[3];

      return String.join(".", head, payload, signature);
    }
    return null;
  }
}
