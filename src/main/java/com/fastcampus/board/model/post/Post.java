package com.fastcampus.board.model.post;

import java.time.ZonedDateTime;

public record Post(Long postId, String body, ZonedDateTime createdDateTime) {
}