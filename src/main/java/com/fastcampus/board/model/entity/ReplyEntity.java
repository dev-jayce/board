package com.fastcampus.board.model.entity;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(
    name = "reply",
    indexes = {
      @Index(name = "reply_userid_idx", columnList = "userid"),
      @Index(name = "reply_postid_idx", columnList = "postid")
    })
@SQLDelete(sql = "UPDATE reply SET deleteddatetime = CURRENT_TIMESTAMP WHERE replyid = ?")
@SQLRestriction("deleteddatetime IS NULL")
public class ReplyEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long replyId;

  @Column(columnDefinition = "TEXT")
  private String body;

  @Column private ZonedDateTime createdDateTime;

  @Column private ZonedDateTime updatedDateTime;

  @Column private ZonedDateTime deletedDateTime;

  @ManyToOne
  @JoinColumn(name = "userid")
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "postid")
  private PostEntity post;

  public ReplyEntity() {}

  public static ReplyEntity of(String body, UserEntity user, PostEntity post) {
    ReplyEntity reply = new ReplyEntity();
    reply.setBody(body);
    reply.setUser(user);
    reply.setPost(post);
    return reply;
  }

  public Long getReplyId() {
    return replyId;
  }

  public void setReplyId(Long replyId) {
    this.replyId = replyId;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
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

  public PostEntity getPost() {
    return post;
  }

  public void setPost(PostEntity post) {
    this.post = post;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ReplyEntity reply)) return false;
    return Objects.equals(getReplyId(), reply.getReplyId())
        && Objects.equals(getBody(), reply.getBody())
        && Objects.equals(getCreatedDateTime(), reply.getCreatedDateTime())
        && Objects.equals(getUpdatedDateTime(), reply.getUpdatedDateTime())
        && Objects.equals(getDeletedDateTime(), reply.getDeletedDateTime())
        && Objects.equals(getUser(), reply.getUser())
        && Objects.equals(getPost(), reply.getPost());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        getReplyId(),
        getBody(),
        getCreatedDateTime(),
        getUpdatedDateTime(),
        getDeletedDateTime(),
        getUser(),
        getPost());
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
