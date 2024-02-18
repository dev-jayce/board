package com.fastcampus.board.model.post;

import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.ZoneId;
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

  public static Post from(PostWithLikingStatusProjection projection) {
    return new Post(
        projection.getPostId(),
        projection.getBody(),
        projection.getRepliesCount(),
        projection.getLikesCount(),
        new User(
            projection.getUserId(),
            projection.getUsername(),
            projection.getUserProfile(),
            projection.getUserDescription(),
            projection.getUserFollowersCount(),
            projection.getUserFollowingsCount(),
            projection.getUserCreatedDateTime().atZone(ZoneId.systemDefault()),
            projection.getUpdatedDateTime().atZone(ZoneId.systemDefault()),
            null),
        projection.getCreatedDateTime().atZone(ZoneId.systemDefault()),
        projection.getUpdatedDateTime().atZone(ZoneId.systemDefault()),
        null,
        projection.getIsLiking());
  }
}
