package com.fastcampus.board.controller;

import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.post.PostPatchRequestBody;
import com.fastcampus.board.model.post.PostPostRequestBody;
import com.fastcampus.board.model.user.LikedUser;
import com.fastcampus.board.service.PostService;
import com.fastcampus.board.service.UserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

  private static final Logger logger = LoggerFactory.getLogger(PostController.class);

  @Autowired private PostService postService;

  @Autowired private UserService userService;

  @GetMapping
  public ResponseEntity<List<Post>> getPosts(Authentication authentication) {
    logger.info("GET /api/v1/posts");
    var posts = postService.getPosts((UserEntity) authentication.getPrincipal());
    return ResponseEntity.ok(posts);
  }

  @GetMapping("/{postId}")
  public ResponseEntity<Post> getPostByPostId(
      @PathVariable Long postId, Authentication authentication) {
    logger.info("GET /api/v1/posts/{}", postId);
    var post = postService.getPostByPostId(postId, (UserEntity) authentication.getPrincipal());
    return ResponseEntity.ok(post);
  }

  @PostMapping
  public ResponseEntity<Post> createPost(
      @RequestBody PostPostRequestBody postPostRequestBody, Authentication authentication) {
    logger.info("POST /api/v1/posts");
    var post =
        postService.createPost(postPostRequestBody, (UserEntity) authentication.getPrincipal());
    return ResponseEntity.ok(post);
  }

  @PatchMapping("/{postId}")
  public ResponseEntity<Post> updatePost(
      @PathVariable Long postId,
      @RequestBody PostPatchRequestBody postPatchRequestBody,
      Authentication authentication) {
    logger.info("PATCH /api/v1/posts/{}", postId);
    var post =
        postService.updatePost(
            postId, postPatchRequestBody, (UserEntity) authentication.getPrincipal());
    return ResponseEntity.ok(post);
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(@PathVariable Long postId, Authentication authentication) {
    logger.info("DELETE /api/v1/posts/{}", postId);
    postService.deletePost(postId, (UserEntity) authentication.getPrincipal());
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{postId}/likes")
  public ResponseEntity<Post> toggleLike(@PathVariable Long postId, Authentication authentication) {
    var post = postService.toggleLike(postId, (UserEntity) authentication.getPrincipal());
    return new ResponseEntity<>(post, HttpStatus.OK);
  }

  @GetMapping("/{postId}/liked-users")
  public ResponseEntity<List<LikedUser>> getLikedUsersByPostId(
      @PathVariable Long postId, Authentication authentication) {
    var likedUsers =
        userService.getLikedUsersByPostId(postId, (UserEntity) authentication.getPrincipal());
    return new ResponseEntity<>(likedUsers, HttpStatus.OK);
  }
}
