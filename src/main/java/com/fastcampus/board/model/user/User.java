package com.fastcampus.board.model.user;

import com.fastcampus.board.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record User(
    Long userId,
    String username,
    String profile,
    String description,
    ZonedDateTime createdDateTime,
    ZonedDateTime updatedDateTime) {

  public static User from(UserEntity user) {
    return new User(
        user.getUserId(),
        user.getUsername(),
        user.getProfile(),
        user.getDescription(),
        user.getCreatedDateTime(),
        user.getUpdatedDateTime());
  }
}
