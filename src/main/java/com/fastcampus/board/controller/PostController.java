package com.fastcampus.board.controller;

import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.post.PostPatchRequestBody;
import com.fastcampus.board.model.post.PostPostRequestBody;
import com.fastcampus.board.service.PostService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

  private static final Logger logger = LoggerFactory.getLogger(PostController.class);
  @Autowired private PostService postService;

  @GetMapping
  public ResponseEntity<List<Post>> getPosts() {
    logger.info("GET /api/v1/posts");
    var posts = postService.getPosts();
    return ResponseEntity.ok(posts);
  }

  @GetMapping("/{postId}")
  public ResponseEntity<Post> getPostByPostId(@PathVariable Long postId) {
    logger.info("GET /api/v1/posts/{}", postId);
    var post = postService.getPostByPostId(postId);
    return ResponseEntity.ok(post);
  }

  @PostMapping
  public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody) {
    logger.info("POST /api/v1/posts");
    var post = postService.createPost(postPostRequestBody);
    return ResponseEntity.ok(post);
  }

  @PatchMapping("/{postId}")
  public ResponseEntity<Post> updatePost(
      @PathVariable Long postId, @RequestBody PostPatchRequestBody postPatchRequestBody) {
    logger.info("PATCH /api/v1/posts/{}", postId);
    var post = postService.updatePost(postId, postPatchRequestBody);
    return ResponseEntity.ok(post);
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
    logger.info("DELETE /api/v1/posts/{}", postId);
    postService.deletePost(postId);
    return ResponseEntity.noContent().build();
  }
}
