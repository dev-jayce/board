package com.fastcampus.board.repository;

import com.fastcampus.board.model.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername(String username);

  List<UserEntity> findByUsernameContaining(String username);
}
