package com.fastcampus.board.controller;

import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.post.PostPatchRequestBody;
import com.fastcampus.board.model.post.PostPostRequestBody;
import com.fastcampus.board.service.PostService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

  @Autowired private PostService postService;

  @GetMapping
  public ResponseEntity<List<Post>> getPosts() {
    var posts = postService.getPosts();
    return ResponseEntity.ok(posts);
  }

  @GetMapping("/{postId}")
  public ResponseEntity<Post> getPostByPostId(@PathVariable Long postId) {
    Optional<Post> matchingPost = postService.getPostById(postId);
    return matchingPost.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody) {
    var post = postService.createPost(postPostRequestBody);
    return ResponseEntity.ok(post);
  }

  @PatchMapping("/{postId}")
  public ResponseEntity<Post> updatePost(
      @PathVariable Long postId, @RequestBody PostPatchRequestBody postPatchRequestBody) {
    var post = postService.updatePost(postId, postPatchRequestBody);
    return ResponseEntity.ok(post);
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
    postService.deletePost(postId);
    return ResponseEntity.noContent().build();
  }
}
