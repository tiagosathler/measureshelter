package com.agrotechfields.measureshelter.dto.response;

import com.agrotechfields.measureshelter.domain.Role;
import com.agrotechfields.measureshelter.domain.User;
import org.bson.types.ObjectId;

/**
 * The Class UserResponseDto.
 */
public class UserResponseDto {

  /** The username. */
  private String username;
  
  /** The id. */
  private ObjectId id;
  
  /** The role. */
  private Role role;
  
  /** The is account non expired. */
  private boolean isAccountNonExpired;
  
  /** The is account non locked. */
  private boolean isAccountNonLocked;
  
  /** The is credentials non expired. */
  private boolean isCredentialsNonExpired;
  
  /** The is enable. */
  private boolean isEnable;
  
  /**
   * Instantiates a new user response dto.
   *
   * @param user the user
   */
  public UserResponseDto(User user) {
    this.username = user.getUsername();
    this.id = user.getId();
    this.role = user.getRole();
    this.isAccountNonExpired = user.isAccountNonExpired();
    this.isAccountNonLocked = user.isAccountNonLocked();
    this.isCredentialsNonExpired = user.isCredentialsNonExpired();
    this.isEnable = user.isEnabled();
  }

  /**
   * Gets the username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Gets the id.
   *
   * @return the id
   */
  public String getId() {
    return id.toHexString();
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
   * Checks if is account non expired.
   *
   * @return true, if is account non expired
   */
  public boolean isAccountNonExpired() {
    return isAccountNonExpired;
  }

  /**
   * Checks if is account non locked.
   *
   * @return true, if is account non locked
   */
  public boolean isAccountNonLocked() {
    return isAccountNonLocked;
  }

  /**
   * Checks if is credentials non expired.
   *
   * @return true, if is credentials non expired
   */
  public boolean isCredentialsNonExpired() {
    return isCredentialsNonExpired;
  }

  /**
   * Checks if is enable.
   *
   * @return true, if is enable
   */
  public boolean isEnable() {
    return isEnable;
  }
}
