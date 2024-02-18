package com.fastcampus.board.model.user;

import java.time.Instant;

public interface UserWithFollowingStatusProjection {
  Long getUserId();

  String getUsername();

  String getProfile();

  String getDescription();

  Long getFollowersCount();

  Long getFollowingsCount();

  Instant getCreatedDateTime();

  Instant getUpdatedDateTime();

  Boolean getIsFollowing();
}
