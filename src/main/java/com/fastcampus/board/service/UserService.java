package com.fastcampus.board.service;

import com.fastcampus.board.exception.follow.FollowAlreadyExistsException;
import com.fastcampus.board.exception.follow.FollowNotFoundException;
import com.fastcampus.board.exception.follow.InvalidFollowException;
import com.fastcampus.board.exception.post.PostNotFoundException;
import com.fastcampus.board.exception.user.UserAlreadyExistsException;
import com.fastcampus.board.exception.user.UserNotAllowedException;
import com.fastcampus.board.exception.user.UserNotFoundException;
import com.fastcampus.board.model.entity.FollowEntity;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.user.*;
import com.fastcampus.board.repository.FollowEntityRepository;
import com.fastcampus.board.repository.PostEntityRepository;
import com.fastcampus.board.repository.UserEntityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

  @Autowired private UserEntityRepository userEntityRepository;

  @Autowired private PostEntityRepository postEntityRepository;

  @Autowired private FollowEntityRepository followEntityRepository;

  @Autowired private BCryptPasswordEncoder passwordEncoder;

  @Autowired private JwtService jwtService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userEntityRepository
        .findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(username));
  }

  public User signUp(String username, String password) {
    userEntityRepository
        .findByUsername(username)
        .ifPresent(
            user -> {
              throw new UserAlreadyExistsException();
            });

    var userEntity = UserEntity.of(username, passwordEncoder.encode(password));
    userEntityRepository.save(userEntity);

    return User.from(userEntity);
  }

  public UserAuthenticationResponse login(String username, String password) {
    var userEntity =
        userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

    if (passwordEncoder.matches(password, userEntity.getPassword())) {
      var accessToken = jwtService.generateToken(userEntity);
      return new UserAuthenticationResponse(accessToken);
    } else {
      throw new UserNotFoundException();
    }
  }

  public List<User> getUsers(String query, UserEntity currentUser) {
    var projections =
        userEntityRepository.findUsersByOptionalUsernameWithFollowingStatus(
            query, currentUser.getUserId());
    return projections.stream().map(User::from).toList();
  }

  public User getUser(String username, UserEntity currentUser) {
    var user =
        userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    return getUserWithFollowingStatus(user, currentUser);
  }

  public List<LikedUser> getLikedUsersByPostId(Long postId, UserEntity currentUser) {
    var post = postEntityRepository.findById(postId).orElseThrow(PostNotFoundException::new);

    var projections =
        userEntityRepository.findUsersWhoLikedPostByPostIdWithFollowingStatus(
            post.getPostId(), currentUser.getUserId());

    return projections.stream().map(LikedUser::from).toList();
  }

  public List<LikedUser> getLikedUsersByUser(String username, UserEntity currentUser) {
    var user =
        userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

    var projections =
        userEntityRepository.findUsersWhoLikedPostByUserIdWithFollowingStatus(
            user.getUserId(), currentUser.getUserId());

    return projections.stream().map(LikedUser::from).toList();
  }

  public User updateUser(
      String username, UserPatchRequestBody userPatchRequestBody, UserEntity currentUser) {
    var user =
        userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    if (!currentUser.equals(user)) {
      throw new UserNotAllowedException();
    }

    if (userPatchRequestBody.description() != null) {
      user.setDescription(userPatchRequestBody.description());
    }

    return User.from(userEntityRepository.save(user));
  }

  @Transactional
  public User follow(String username, UserEntity currentUser) {
    var following =
        userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    if (currentUser.equals(following)) {
      throw new InvalidFollowException("A user cannot follow themselves.");
    }

    followEntityRepository
        .findByFollowerAndFollowing(currentUser, following)
        .ifPresent(
            follow -> {
              throw new FollowAlreadyExistsException(currentUser, following);
            });
    followEntityRepository.save(FollowEntity.of(currentUser, following));

    following.setFollowersCount(following.getFollowersCount() + 1);
    currentUser.setFollowingsCount(currentUser.getFollowingsCount() + 1);

    userEntityRepository.save(following);
    userEntityRepository.save(currentUser);

    return User.from(following, true);
  }

  @Transactional
  public User unFollow(String username, UserEntity currentUser) {
    var following =
        userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    if (currentUser.equals(following)) {
      throw new InvalidFollowException("A user cannot unfollow themselves.");
    }

    var follow =
        followEntityRepository
            .findByFollowerAndFollowing(currentUser, following)
            .orElseThrow(() -> new FollowNotFoundException(currentUser, following));
    followEntityRepository.delete(follow);

    long newFollowersCount = Math.max(0, following.getFollowersCount() - 1);
    long newFollowingsCount = Math.max(0, currentUser.getFollowingsCount() - 1);
    following.setFollowersCount(newFollowersCount);
    currentUser.setFollowingsCount(newFollowingsCount);

    userEntityRepository.save(following);
    userEntityRepository.save(currentUser);

    return User.from(following, false);
  }

  public List<Follower> getFollowersByUsername(String username, UserEntity currentUser) {
    var following =
        userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    var projections =
        userEntityRepository.findFollowersByFollowingUserIdWithFollowingStatus(
            following.getUserId(), currentUser.getUserId());
    return projections.stream().map(Follower::from).toList();
  }

  public List<User> getFollowingsByUsername(String username, UserEntity currentUser) {
    var follower =
        userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    var projections =
        userEntityRepository.findFollowingsByFollowerUserIdWithFollowingStatus(
            follower.getUserId(), currentUser.getUserId());
    return projections.stream().map(User::from).toList();
  }

  private User getUserWithFollowingStatus(UserEntity user, UserEntity currentUser) {
    var isFollowing =
        followEntityRepository.findByFollowerAndFollowing(currentUser, user).isPresent();
    return User.from(user, isFollowing);
  }
}
