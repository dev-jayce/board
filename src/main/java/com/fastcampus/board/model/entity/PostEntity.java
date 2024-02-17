package com.fastcampus.board.model.entity;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(
    name = "post",
    indexes = {@Index(name = "post_userid_idx", columnList = "userid")})
@SQLDelete(sql = "UPDATE \"post\" SET deleteddatetime = CURRENT_TIMESTAMP WHERE postid = ?")
// Deprecated in Hibernate 6.3
// @Where(clause = "deletedDateTime IS NULL")
@SQLRestriction("deleteddatetime IS NULL")
public class PostEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long postId;

  @Column(columnDefinition = "TEXT")
  private String body;

  @Column private Long repliesCount = 0L;

  @Column private Long likesCount = 0L;

  @Column private ZonedDateTime createdDateTime;

  @Column private ZonedDateTime updatedDateTime;

  @Column private ZonedDateTime deletedDateTime;

  @ManyToOne
  @JoinColumn(name = "userid")
  private UserEntity user;

  public PostEntity() {}

  public static PostEntity of(String body, UserEntity user) {
    PostEntity post = new PostEntity();
    post.setBody(body);
    post.setUser(user);
    return post;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Long getRepliesCount() {
    return repliesCount;
  }

  public void setRepliesCount(Long repliesCount) {
    this.repliesCount = repliesCount;
  }

  public Long getLikesCount() {
    return likesCount;
  }

  public void setLikesCount(Long likesCount) {
    this.likesCount = likesCount;
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

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PostEntity that)) return false;
    return Objects.equals(getPostId(), that.getPostId())
        && Objects.equals(getBody(), that.getBody())
        && Objects.equals(getRepliesCount(), that.getRepliesCount())
        && Objects.equals(getLikesCount(), that.getLikesCount())
        && Objects.equals(getCreatedDateTime(), that.getCreatedDateTime())
        && Objects.equals(getUpdatedDateTime(), that.getUpdatedDateTime())
        && Objects.equals(getDeletedDateTime(), that.getDeletedDateTime())
        && Objects.equals(getUser(), that.getUser());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        getPostId(),
        getBody(),
        getRepliesCount(),
        getLikesCount(),
        getCreatedDateTime(),
        getUpdatedDateTime(),
        getDeletedDateTime(),
        getUser());
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
