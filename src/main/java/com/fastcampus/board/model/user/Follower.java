package com.fastcampus.board.model.user;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public record Follower(
    Long userId,
    String username,
    String profile,
    String description,
    Long followersCount,
    Long followingsCount,
    ZonedDateTime createdDateTime,
    ZonedDateTime updatedDateTime,
    ZonedDateTime followedDateTime,
    Boolean isFollowing) {

  public static Follower from(FollowerWithFollowingStatusProjection projection) {
    return new Follower(
        projection.getUserId(),
        projection.getUsername(),
        projection.getProfile(),
        projection.getDescription(),
        projection.getFollowersCount(),
        projection.getFollowingsCount(),
        projection.getCreatedDateTime().atZone(ZoneId.systemDefault()),
        projection.getUpdatedDateTime().atZone(ZoneId.systemDefault()),
        projection.getFollowedDateTime().atZone(ZoneId.systemDefault()),
        projection.getIsFollowing());
  }
}
