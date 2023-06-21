package com.agrotechfields.measureshelter.security;

import com.agrotechfields.measureshelter.domain.Role;
import com.agrotechfields.measureshelter.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

/**
 * The Class SecurityConfiguration.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Value("${endpoint.measure}")
  private String measureEndpoint;

  @Value("${endpoint.isle}")
  private String isleEndpoint;

  @Value("${endpoint.user}")
  private String userEndpoint;

  @Value("${endpoint.login}")
  private String loginEndpoint;

  @Value("${endpoint.image}")
  private String imageEndpoint;

  /** The Constant HEALTH. */
  private static final String HEALTH = "/actuator/health";

  /** The Constant ACTUATOR. */
  private static final String ACTUATOR = "/actuator/**";


  /** The Constant ROLE_ADMIN. */
  private static final String ROLE_ADMIN = Role.ROLE_ADMIN.name();

  /** The Constant ROLE_USER. */
  private static final String ROLE_USER = Role.ROLE_USER.name();

  /** The Constant ROLE_ISLE. */
  private static final String ROLE_ISLE = Role.ROLE_ISLE.name();

  /** The Constant ROLE_SAT. */
  private static final String ROLE_SAT = Role.ROLE_SAT.name();

  /**
   * Security filter chain.
   *
   * @param httpSecurity the http security
   * @param jwtSecurityFilter the jwt security filter
   * @param userService the user service
   * @return the security filter chain
   * @throws Exception the exception
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
      JwtSecurityFilter jwtSecurityFilter, UserService userService) throws Exception {
    final String login = loginEndpoint;
    final String user = userEndpoint + "/**";
    final String userIsle = userEndpoint + "/isle/**";
    final String isle = isleEndpoint + "/**";
    final String measure = measureEndpoint + "/**";
    final String image = imageEndpoint + "/**";

    return httpSecurity
        .headers(h -> {
          h.frameOptions(f -> f.sameOrigin());
          h.xssProtection(x -> x.headerValue(XXssProtectionHeaderWriter.HeaderValue.DISABLED));
        })
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.disable())
        .httpBasic(h -> h.disable())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(req -> {
          // Permit all for HEALTH and LOGIN endpoints:
          req.requestMatchers(HttpMethod.GET, HEALTH).permitAll();
          req.requestMatchers(HttpMethod.POST, login).permitAll();

          // Security authority for ACTUATOR endpoints:
          req.requestMatchers(ACTUATOR).hasAuthority(ROLE_ADMIN);

          // Security authorities for USER endpoints:
          req.requestMatchers(HttpMethod.POST, user).hasAuthority(ROLE_ADMIN);
          req.requestMatchers(HttpMethod.GET, user).hasAnyAuthority(ROLE_ADMIN, ROLE_USER);
          req.requestMatchers(HttpMethod.PUT, userIsle).hasAuthority(ROLE_ADMIN);
          req.requestMatchers(HttpMethod.PUT, user).hasAnyAuthority(ROLE_ADMIN, ROLE_USER);
          req.requestMatchers(HttpMethod.PATCH, user).hasAuthority(ROLE_ADMIN);
          req.requestMatchers(HttpMethod.DELETE, user).hasAuthority(ROLE_ADMIN);

          // Security authorities for ISLE endpoints:
          req.requestMatchers(HttpMethod.POST, isle).hasAuthority(ROLE_ADMIN);
          req.requestMatchers(HttpMethod.GET, isle).hasAnyAuthority(ROLE_ADMIN, ROLE_USER,
              ROLE_ISLE);
          req.requestMatchers(HttpMethod.PUT, isle).hasAuthority(ROLE_ADMIN);
          req.requestMatchers(HttpMethod.PATCH, isle).hasAnyAuthority(ROLE_ADMIN, ROLE_USER);
          req.requestMatchers(HttpMethod.DELETE, isle).hasAuthority(ROLE_ADMIN);

          // Security authorities for MEASURE endpoints:
          req.requestMatchers(HttpMethod.POST, measure).hasAuthority(ROLE_ISLE);
          req.requestMatchers(HttpMethod.GET, measure).hasAnyAuthority(ROLE_ADMIN, ROLE_USER,
              ROLE_ISLE);
          req.requestMatchers(HttpMethod.PUT, measure).hasAuthority(ROLE_ADMIN);
          req.requestMatchers(HttpMethod.DELETE, measure).hasAuthority(ROLE_ADMIN);

          // Security authorities for IMAGE endpoints:
          req.requestMatchers(HttpMethod.POST, image).hasAuthority(ROLE_SAT);
          req.requestMatchers(HttpMethod.GET, image).hasAnyAuthority(ROLE_ADMIN, ROLE_USER);
          req.requestMatchers(HttpMethod.DELETE, image).hasAuthority(ROLE_ADMIN);

          // Security any authorities for others endpoints:
          req.anyRequest().authenticated();
        })
        .userDetailsService(userService)
        .addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  /**
   * Authentication manager.
   *
   * @param configuration the configuration
   * @return the authentication manager
   * @throws Exception the exception
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  /**
   * Password encoder.
   *
   * @return the password encoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
