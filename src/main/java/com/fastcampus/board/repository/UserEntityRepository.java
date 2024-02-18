package com.fastcampus.board.repository;

import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.user.FollowerWithFollowingStatusProjection;
import com.fastcampus.board.model.user.UserWhoLikedPostWithFollowingStatusProjection;
import com.fastcampus.board.model.user.UserWithFollowingStatusProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername(String username);

  List<UserEntity> findByUsernameContaining(String username);

  @Query(
      value =
          """
              SELECT
                  u.userid AS userId,
                  u.username AS username,
                  u.profile AS profile,
                  u.description AS description,
                  u.followerscount AS followersCount,
                  u.followingscount AS followingsCount,
                  u.createddatetime AS createdDateTime,
                  u.updateddatetime AS updatedDateTime,
                  CASE WHEN f.followid IS NOT NULL THEN TRUE ELSE FALSE END AS isFollowing
              FROM "user" u
              LEFT JOIN follow f ON u.userid = f.following AND f.follower = :currentUserId
              WHERE (:username IS NULL OR :username = '') OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))
              """,
      nativeQuery = true)
  List<UserWithFollowingStatusProjection> findUsersByOptionalUsernameWithFollowingStatus(
      @Param("username") String username, @Param("currentUserId") Long currentUserId);

  @Query(
      value =
          """
              SELECT
                  u.userid AS userId,
                  u.username AS username,
                  u.profile AS profile,
                  u.description AS description,
                  u.followerscount AS followersCount,
                  u.followingscount AS followingsCount,
                  u.createddatetime AS createdDateTime,
                  u.updateddatetime AS updatedDateTime,
                  CASE WHEN f2.follower IS NOT NULL THEN TRUE ELSE FALSE END AS isFollowing
              FROM
                  "user" u
              INNER JOIN follow f ON u.userid = f.following
              LEFT JOIN follow f2 ON f.following = f2.following AND f2.follower = :currentUserId
              WHERE
                  f.follower = :followerUserId
              """,
      nativeQuery = true)
  List<UserWithFollowingStatusProjection> findFollowingsByFollowerUserIdWithFollowingStatus(
      @Param("followerUserId") Long followerUserId, @Param("currentUserId") Long currentUserId);

  @Query(
      value =
          """
              SELECT
                  u.userid AS userId,
                  u.username AS username,
                  u.profile AS profile,
                  u.description AS description,
                  u.followerscount AS followersCount,
                  u.followingscount AS followingsCount,
                  u.createddatetime AS createdDateTime,
                  u.updateddatetime AS updatedDateTime,
                  f.createddatetime AS followedDateTime,
                  CASE WHEN f2.follower IS NOT NULL THEN TRUE ELSE FALSE END AS isFollowing
              FROM
                  "user" u
              INNER JOIN follow f ON u.userid = f.follower
              LEFT JOIN follow f2 ON f.follower = f2.following AND f2.follower = :currentUserId
              WHERE
                  f.following = :followingUserId
              """,
      nativeQuery = true)
  List<FollowerWithFollowingStatusProjection> findFollowersByFollowingUserIdWithFollowingStatus(
      @Param("followingUserId") Long followingUserId, @Param("currentUserId") Long currentUserId);

  @Query(
      value =
          """
              SELECT
                  u.userid AS userId,
                  u.username AS username,
                  u.profile AS profile,
                  u.description AS description,
                  u.followerscount AS followersCount,
                  u.followingscount AS followingsCount,
                  u.createddatetime AS createdDateTime,
                  u.updateddatetime AS updatedDateTime,
                  l.createddatetime AS likedDateTime,
                  l.postid AS likedPostId,
                  (CASE WHEN f.followid IS NOT NULL THEN TRUE ELSE FALSE END) AS isFollowing
              FROM
                  "user" u
              INNER JOIN
                  "like" l ON u.userid = l.userid
              INNER JOIN
                  post p ON l.postid = p.postid
              LEFT JOIN
                  "follow" f ON u.userid = f.following AND f.follower = :currentUserId
              WHERE
                  p.postid = :postId
              """,
      nativeQuery = true)
  List<UserWhoLikedPostWithFollowingStatusProjection>
      findUsersWhoLikedPostByPostIdWithFollowingStatus(
          @Param("postId") Long postId, @Param("currentUserId") Long currentUserId);

  @Query(
      value =
          """
              SELECT
                  u.userid AS userId,
                  u.username AS username,
                  u.profile AS profile,
                  u.description AS description,
                  u.followerscount AS followersCount,
                  u.followingscount AS followingsCount,
                  u.createddatetime AS createdDateTime,
                  u.updateddatetime AS updatedDateTime,
                  l.createddatetime AS likedDateTime,
                  l.postid AS likedPostId,
                  (CASE WHEN f.followid IS NOT NULL THEN TRUE ELSE FALSE END) AS isFollowing
              FROM
                  "user" u
              INNER JOIN
                  "like" l ON u.userid = l.userid
              INNER JOIN
                  post p ON l.postid = p.postid
              LEFT JOIN
                  "follow" f ON u.userid = f.following AND f.follower = :currentUserId
              WHERE
                  p.userid = :userId
              """,
      nativeQuery = true)
  List<UserWhoLikedPostWithFollowingStatusProjection>
      findUsersWhoLikedPostByUserIdWithFollowingStatus(
          @Param("userId") Long userId, @Param("currentUserId") Long currentUserId);
}
