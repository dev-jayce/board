package com.fastcampus.board.model.post;

import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Post(
    Long postId,
    String body,
    Long repliesCount,
    Long likesCount,
    User user,
    ZonedDateTime createdDateTime,
    ZonedDateTime updatedDateTime,
    ZonedDateTime deletedDateTime,
    Boolean isLiking) {
  public static Post from(PostEntity postEntity) {
    return new Post(
        postEntity.getPostId(),
        postEntity.getBody(),
        postEntity.getRepliesCount(),
        postEntity.getLikesCount(),
        User.from(postEntity.getUser()),
        postEntity.getCreatedDateTime(),
        postEntity.getUpdatedDateTime(),
        postEntity.getDeletedDateTime(),
        null);
  }

  public static Post from(PostEntity postEntity, Boolean isLiking) {
    return new Post(
        postEntity.getPostId(),
        postEntity.getBody(),
        postEntity.getRepliesCount(),
        postEntity.getLikesCount(),
        User.from(postEntity.getUser()),
        postEntity.getCreatedDateTime(),
        postEntity.getUpdatedDateTime(),
        postEntity.getDeletedDateTime(),
        isLiking);
  }
}
