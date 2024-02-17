package com.fastcampus.board.repository;

import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.entity.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {
  List<PostEntity> findByUser(UserEntity user);
}
