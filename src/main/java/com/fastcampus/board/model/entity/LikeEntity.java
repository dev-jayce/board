package com.fastcampus.board.model.entity;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(
    name = "\"like\"",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"userid", "postid"})})
public class LikeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long likeId;

  @ManyToOne
  @JoinColumn(name = "userid")
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "postid")
  private PostEntity post;

  @Column private ZonedDateTime createdDateTime;

  public LikeEntity() {}

  public static LikeEntity of(UserEntity user, PostEntity post) {
    LikeEntity like = new LikeEntity();
    like.setUser(user);
    like.setPost(post);
    return like;
  }

  public Long getLikeId() {
    return likeId;
  }

  public void setLikeId(Long likeId) {
    this.likeId = likeId;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public PostEntity getPost() {
    return post;
  }

  public void setPost(PostEntity post) {
    this.post = post;
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
    if (!(o instanceof LikeEntity that)) return false;
    return Objects.equals(getLikeId(), that.getLikeId())
        && Objects.equals(getUser(), that.getUser())
        && Objects.equals(getPost(), that.getPost())
        && Objects.equals(getCreatedDateTime(), that.getCreatedDateTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getLikeId(), getUser(), getPost(), getCreatedDateTime());
  }

  @PrePersist
  private void prePersist() {
    this.createdDateTime = ZonedDateTime.now();
  }
}
