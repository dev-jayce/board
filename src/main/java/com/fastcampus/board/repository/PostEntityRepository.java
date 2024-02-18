package com.fastcampus.board.repository;

import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.post.PostWithLikingStatusProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {
  List<PostEntity> findByUser(UserEntity user);

  @Query(
      value =
          """
               SELECT
                    p.postId AS postId,
                    p.body AS body,
                    p.likesCount AS likesCount,
                    p.repliesCount AS repliesCount,
                    p.createdDateTime AS createdDateTime,
                    p.updatedDateTime AS updatedDateTime,
                    p.deletedDateTime AS deletedDateTime,
                    u.userId AS userId,
                    u.username AS username,
                    u.profile AS userProfile,
                    u.description AS userDescription,
                    u.followersCount AS userFollowersCount,
                    u.followingsCount AS userFollowingsCount,
                    u.createdDateTime AS userCreatedDateTime,
                    u.updatedDateTime AS userUpdatedDateTime,
                    CASE WHEN l.likeId IS NOT NULL THEN TRUE ELSE FALSE END AS isLiking
                FROM
                    Post p
                JOIN
                    "user" u ON p.userId = u.userId
                LEFT JOIN
                    "like" l ON p.postId = l.postId AND l.userId = :currentUserId
                """,
      nativeQuery = true)
  List<PostWithLikingStatusProjection> findPostsWithLikingStatus(
      @Param("currentUserId") Long currentUserId);

  @Query(
      value =
          """
               SELECT
                    p.postId AS postId,
                    p.body AS body,
                    p.likesCount AS likesCount,
                    p.repliesCount AS repliesCount,
                    p.createdDateTime AS createdDateTime,
                    p.updatedDateTime AS updatedDateTime,
                    p.deletedDateTime AS deletedDateTime,
                    u.userId AS userId,
                    u.username AS username,
                    u.profile AS userProfile,
                    u.description AS userDescription,
                    u.followersCount AS userFollowersCount,
                    u.followingsCount AS userFollowingsCount,
                    u.createdDateTime AS userCreatedDateTime,
                    u.updatedDateTime AS userUpdatedDateTime,
                    CASE WHEN l.likeId IS NOT NULL THEN TRUE ELSE FALSE END AS isLiking
                FROM
                    Post p
                JOIN
                    "user" u ON p.userId = u.userId
                LEFT JOIN
                    "like" l ON p.postId = l.postId AND l.userId = :currentUserId
                WHERE p.userId = :userId
                """,
      nativeQuery = true)
  List<PostWithLikingStatusProjection> findPostsByUserIdWithLikingStatus(
      @Param("userId") Long userId, @Param("currentUserId") Long currentUserId);
}
