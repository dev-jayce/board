package com.fastcampus.board.model.entity;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(
    name = "\"user\"",
    indexes = {@Index(name = "user_username_idx", columnList = "username", unique = true)})
@SQLDelete(sql = "UPDATE \"user\" SET deletedDateTime = CURRENT_TIMESTAMP WHERE userId = ?")
@SQLRestriction("deletedDateTime IS NULL")
public class UserEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column private String username;

  @Column private String password;

  @Column private String profile;

  @Column private String description;

  @Column private Long followersCount = 0L;

  @Column private Long followingsCount = 0L;

  @Column private ZonedDateTime createdDateTime;

  @Column private ZonedDateTime updatedDateTime;

  @Column private ZonedDateTime deletedDateTime;

  public UserEntity() {}

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ZonedDateTime getCreatedDateTime() {
    return createdDateTime;
  }

  public void setCreatedDateTime(ZonedDateTime createdDateTime) {
    this.createdDateTime = createdDateTime;
  }

  public ZonedDateTime getUpdatedDateTime() {
    return updatedDateTime;
  }

  public void setUpdatedDateTime(ZonedDateTime updatedDateTime) {
    this.updatedDateTime = updatedDateTime;
  }

  public ZonedDateTime getDeletedDateTime() {
    return deletedDateTime;
  }

  public void setDeletedDateTime(ZonedDateTime deletedDateTime) {
    this.deletedDateTime = deletedDateTime;
  }

  public Long getFollowersCount() {
    return followersCount;
  }

  public void setFollowersCount(Long followersCount) {
    this.followersCount = followersCount;
  }

  public Long getFollowingsCount() {
    return followingsCount;
  }

  public void setFollowingsCount(Long followingsCount) {
    this.followingsCount = followingsCount;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UserEntity user)) return false;
    return Objects.equals(getUserId(), user.getUserId())
        && Objects.equals(getUsername(), user.getUsername())
        && Objects.equals(getPassword(), user.getPassword())
        && Objects.equals(getProfile(), user.getProfile())
        && Objects.equals(getDescription(), user.getDescription())
        && Objects.equals(getFollowersCount(), user.getFollowersCount())
        && Objects.equals(getFollowingsCount(), user.getFollowingsCount())
        && Objects.equals(getCreatedDateTime(), user.getCreatedDateTime())
        && Objects.equals(getUpdatedDateTime(), user.getUpdatedDateTime())
        && Objects.equals(getDeletedDateTime(), user.getDeletedDateTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        getUserId(),
        getUsername(),
        getPassword(),
        getProfile(),
        getDescription(),
        getFollowersCount(),
        getFollowingsCount(),
        getCreatedDateTime(),
        getUpdatedDateTime(),
        getDeletedDateTime());
  }

  public static UserEntity of(String username, String password) {
    var userEntity = new UserEntity();
    userEntity.setUsername(username);
    userEntity.setPassword(password);

    // Set random profile image url
    userEntity.setProfile("https://avatar.iran.liara.run/public/" + new Random().nextInt(100));
    return userEntity;
  }

  @PrePersist
  private void prePersist() {
    this.createdDateTime = ZonedDateTime.now();
    this.updatedDateTime = this.createdDateTime;
  }

  @PreUpdate
  private void preUpdate() {
    this.updatedDateTime = ZonedDateTime.now();
  }
}
