package com.fastcampus.board.model.post;

import java.time.Instant;

public interface PostWithLikingStatusProjection {
  Long getPostId();

  String getBody();

  Long getLikesCount();

  Long getRepliesCount();

  Instant getCreatedDateTime();

  Instant getUpdatedDateTime();

  Instant getDeletedDateTime();

  Long getUserId();

  String getUsername();

  String getUserProfile();

  String getUserDescription();

  Long getUserFollowersCount();

  Long getUserFollowingsCount();

  Instant getUserCreatedDateTime();

  Instant getUserUpdatedDateTime();

  Boolean getIsLiking();
}
