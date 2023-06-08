package com.agrotechfields.measureshelter.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * The Class User.
 */
@Document(collection = "users")
public class User implements UserDetails {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The id. */
  @MongoId
  private ObjectId id;

  /** The username. */
  private String username;

  /** The password. */
  private String password;

  /** The role. */
  private Role role;

  /** The account non expired. */
  private boolean accountNonExpired = true;

  /** The account non locked. */
  private boolean accountNonLocked = true;

  /** The credentials non expired. */
  private boolean credentialsNonExpired = true;

  /** The enabled. */
  private boolean enabled = true;

  /**
   * Instantiates a new user.
   */
  public User() {
  }

  /**
   * Instantiates a new user.
   *
   * @param id the id
   * @param username the username
   * @param password the password
   * @param role the role
   */
  public User(ObjectId id, String username, String password, Role role) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.role = role;
  }

  /**
   * Gets the id.
   *
   * @return the id
   */
  public ObjectId getId() {
    return id;
  }

  /**
   * Sets the id.
   *
   * @param id the new id
   */
  public void setId(ObjectId id) {
    this.id = id;
  }

  /**
   * Gets the username.
   *
   * @return the username
   */
  @Override
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username.
   *
   * @param username the new username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets the password.
   *
   * @return the password
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * Sets the password.
   *
   * @param password the new password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Gets the authorities.
   *
   * @return the authorities
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority(role.name()));
  }

  /**
   * Gets the role.
   *
   * @return the role
   */
  public Role getRole() {
    return role;
  }

  /**
   * Sets the role.
   *
   * @param role the new role
   */
  public void setRole(Role role) {
    this.role = role;
  }
  
  /**
   * Checks if is account non expired.
   *
   * @return true, if is account non expired
   */
  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  /**
   * Sets the account non expired.
   *
   * @param accountNonExpired the new account non expired
   */
  public void setAccountNonExpired(boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
  }

  /**
   * Checks if is account non locked.
   *
   * @return true, if is account non locked
   */
  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  /**
   * Sets the account non locked.
   *
   * @param accountNonLocked the new account non locked
   */
  public void setAccountNonLocked(boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
  }

  /**
   * Checks if is credentials non expired.
   *
   * @return true, if is credentials non expired
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  /**
   * Sets the credentials non expired.
   *
   * @param credentialsNonExpired the new credentials non expired
   */
  public void setCredentialsNonExpired(boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
  }

  /**
   * Checks if is enabled.
   *
   * @return true, if is enabled
   */
  @Override
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Sets the enabled.
   *
   * @param enabled the new enabled
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Hash code.
   *
   * @return the int
   */
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  /**
   * Equals.
   *
   * @param obj the obj
   * @return true, if successful
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof User)) {
      return false;
    }
    User other = (User) obj;
    return Objects.equals(id, other.id);
  }
}
