package com.fastcampus.board.controller;

import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.reply.Reply;
import com.fastcampus.board.model.user.*;
import com.fastcampus.board.service.PostService;
import com.fastcampus.board.service.ReplyService;
import com.fastcampus.board.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
  @Autowired UserService userService;
  @Autowired PostService postService;

  @Autowired ReplyService replyService;

  @GetMapping
  public ResponseEntity<List<User>> getUsers(
      @RequestParam(required = false) String query, Authentication authentication) {
    var user = userService.getUsers(query, (UserEntity) authentication.getPrincipal());
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @GetMapping("/{username}")
  public ResponseEntity<User> getUser(
      @PathVariable String username, Authentication authentication) {
    var user = userService.getUser(username, (UserEntity) authentication.getPrincipal());
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @GetMapping("/{username}/posts")
  public ResponseEntity<List<Post>> getPostsByUser(
      @PathVariable String username, Authentication authentication) {
    var posts =
        postService.getPostsByUsername(username, (UserEntity) authentication.getPrincipal());
    return new ResponseEntity<>(posts, HttpStatus.OK);
  }

  @GetMapping("/{username}/replies")
  public ResponseEntity<List<Reply>> getRepliesByUser(@PathVariable String username) {
    var replies = replyService.getRepliesByUser(username);
    return new ResponseEntity<>(replies, HttpStatus.OK);
  }

  @GetMapping("/{username}/liked-users")
  public ResponseEntity<List<LikedUser>> getLikedUsersByUser(
      @PathVariable String username, Authentication authentication) {
    var likedUsers =
        userService.getLikedUsersByUser(username, (UserEntity) authentication.getPrincipal());
    return new ResponseEntity<>(likedUsers, HttpStatus.OK);
  }

  @PatchMapping("/{username}")
  public ResponseEntity<User> updateUser(
      @PathVariable String username,
      @RequestBody UserPatchRequestBody requestBody,
      Authentication authentication) {
    var user =
        userService.updateUser(username, requestBody, (UserEntity) authentication.getPrincipal());
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @GetMapping("/{username}/followers")
  public ResponseEntity<List<Follower>> getFollowersByUser(
      @PathVariable String username, Authentication authentication) {
    var followers =
        userService.getFollowersByUsername(username, (UserEntity) authentication.getPrincipal());
    return new ResponseEntity<>(followers, HttpStatus.OK);
  }

  @GetMapping("/{username}/followings")
  public ResponseEntity<List<User>> getFollowingsByUser(
      @PathVariable String username, Authentication authentication) {
    var followings =
        userService.getFollowingsByUsername(username, (UserEntity) authentication.getPrincipal());
    return new ResponseEntity<>(followings, HttpStatus.OK);
  }

  @PostMapping("/{username}/follows")
  public ResponseEntity<User> follow(@PathVariable String username, Authentication authentication) {
    var user = userService.follow(username, (UserEntity) authentication.getPrincipal());
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @DeleteMapping("/{username}/follows")
  public ResponseEntity<User> unfollow(
      @PathVariable String username, Authentication authentication) {
    var user = userService.unFollow(username, (UserEntity) authentication.getPrincipal());
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<User> signUp(@Valid @RequestBody UserSignUpRequestBody requestBody) {
    var user = userService.signUp(requestBody.username(), requestBody.password());
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PostMapping("/authenticate")
  public ResponseEntity<UserAuthenticationResponse> authenticate(
      @Valid @RequestBody UserLoginRequestBody requestBody) {
    var response = userService.login(requestBody.username(), requestBody.password());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
