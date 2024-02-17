package com.fastcampus.board.repository;

import com.fastcampus.board.model.entity.LikeEntity;
import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {
  List<LikeEntity> findByUser(UserEntity user);

  List<LikeEntity> findByPost(PostEntity post);

  Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);
}
