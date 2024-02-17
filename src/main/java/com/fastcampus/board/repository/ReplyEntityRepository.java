package com.fastcampus.board.repository;

import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.entity.ReplyEntity;
import com.fastcampus.board.model.entity.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyEntityRepository extends JpaRepository<ReplyEntity, Long> {
  List<ReplyEntity> findByUser(UserEntity user);

  List<ReplyEntity> findByPost(PostEntity post);
}
