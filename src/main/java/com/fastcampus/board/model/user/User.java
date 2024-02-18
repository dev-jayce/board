package com.fastcampus.board.model.user;

import com.fastcampus.board.model.entity.UserEntity;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public record User(
    Long userId,
    String username,
    String profile,
    String description,
    Long followersCount,
    Long followingsCount,
    ZonedDateTime createdDateTime,
    ZonedDateTime updatedDateTime,
    Boolean isFollowing) {

  public static User from(UserEntity user) {
    return new User(
        user.getUserId(),
        user.getUsername(),
        user.getProfile(),
        user.getDescription(),
        user.getFollowersCount(),
        user.getFollowingsCount(),
        user.getCreatedDateTime(),
        user.getUpdatedDateTime(),
        null);
  }

  public static User from(UserEntity user, Boolean isFollowing) {
    return new User(
        user.getUserId(),
        user.getUsername(),
        user.getProfile(),
        user.getDescription(),
        user.getFollowersCount(),
        user.getFollowingsCount(),
        user.getCreatedDateTime(),
        user.getUpdatedDateTime(),
        isFollowing);
  }

  public static User from(UserWithFollowingStatusProjection projection) {
    return new User(
        projection.getUserId(),
        projection.getUsername(),
        projection.getProfile(),
        projection.getDescription(),
        projection.getFollowersCount(),
        projection.getFollowingsCount(),
        projection.getCreatedDateTime().atZone(ZoneId.systemDefault()),
        projection.getUpdatedDateTime().atZone(ZoneId.systemDefault()),
        projection.getIsFollowing());
  }
}
