package com.fastcampus.board.model.user;

import java.time.Instant;

public interface FollowerWithFollowingStatusProjection {
  Long getUserId();

  String getUsername();

  String getProfile();

  String getDescription();

  Long getFollowersCount();

  Long getFollowingsCount();

  Instant getCreatedDateTime();

  Instant getUpdatedDateTime();

  Instant getFollowedDateTime();

  Boolean getIsFollowing();
}
