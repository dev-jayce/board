package com.fastcampus.board.exception.follow;

import com.fastcampus.board.exception.ClientErrorException;
import com.fastcampus.board.model.entity.UserEntity;
import org.springframework.http.HttpStatus;

public class FollowAlreadyExistsException extends ClientErrorException {

  public FollowAlreadyExistsException() {
    super(HttpStatus.CONFLICT, "Follow already exists.");
  }

  public FollowAlreadyExistsException(UserEntity follower, UserEntity following) {
    super(
        HttpStatus.CONFLICT,
        "Follow with follower "
            + follower.getUsername()
            + " and following "
            + following.getUsername()
            + " already exists.");
  }
}
