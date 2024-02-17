package com.fastcampus.board.model.entity;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(
    name = "follow",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"follower", "following"})})
public class FollowEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long followId;

  @ManyToOne
  @JoinColumn(name = "follower")
  private UserEntity follower;

  @ManyToOne
  @JoinColumn(name = "following")
  private UserEntity following;

  @Column private ZonedDateTime createdDateTime;

  public FollowEntity() {}

  public static FollowEntity of(UserEntity follower, UserEntity following) {
    FollowEntity follow = new FollowEntity();
    follow.setFollower(follower);
    follow.setFollowing(following);
    return follow;
  }

  public Long getFollowId() {
    return followId;
  }

  public void setFollowId(Long followId) {
    this.followId = followId;
  }

  public UserEntity getFollower() {
    return follower;
  }

  public void setFollower(UserEntity follower) {
    this.follower = follower;
  }

  public UserEntity getFollowing() {
    return following;
  }

  public void setFollowing(UserEntity following) {
    this.following = following;
  }

  public ZonedDateTime getCreatedDateTime() {
    return createdDateTime;
  }

  public void setCreatedDateTime(ZonedDateTime createdDateTime) {
    this.createdDateTime = createdDateTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof FollowEntity that)) return false;
    return Objects.equals(getFollowId(), that.getFollowId())
        && Objects.equals(getFollower(), that.getFollower())
        && Objects.equals(getFollowing(), that.getFollowing())
        && Objects.equals(getCreatedDateTime(), that.getCreatedDateTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getFollowId(), getFollower(), getFollowing(), getCreatedDateTime());
  }

  @PrePersist
  private void prePersist() {
    this.createdDateTime = ZonedDateTime.now();
  }
}
