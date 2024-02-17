package com.fastcampus.board.repository;

import com.fastcampus.board.model.entity.FollowEntity;
import com.fastcampus.board.model.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowEntityRepository extends JpaRepository<FollowEntity, Long> {
  List<FollowEntity> findByFollower(UserEntity follower);

  List<FollowEntity> findByFollowing(UserEntity following);

  Optional<FollowEntity> findByFollowerAndFollowing(UserEntity user, UserEntity following);
}
