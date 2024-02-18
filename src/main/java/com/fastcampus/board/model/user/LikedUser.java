package com.fastcampus.board.model.user;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public record LikedUser(
    Long userId,
    String username,
    String profile,
    String description,
    Long followersCount,
    Long followingsCount,
    ZonedDateTime createdDateTime,
    ZonedDateTime updatedDateTime,
    ZonedDateTime likedDateTime,
    Long likedPostId,
    Boolean isFollowing) {

  public static LikedUser from(UserWhoLikedPostWithFollowingStatusProjection projection) {
    return new LikedUser(
        projection.getUserId(),
        projection.getUsername(),
        projection.getProfile(),
        projection.getDescription(),
        projection.getFollowersCount(),
        projection.getFollowingsCount(),
        projection.getCreatedDateTime().atZone(ZoneId.systemDefault()),
        projection.getUpdatedDateTime().atZone(ZoneId.systemDefault()),
        projection.getLikedDateTime().atZone(ZoneId.systemDefault()),
        projection.getLikedPostId(),
        projection.getIsFollowing());
  }
}
