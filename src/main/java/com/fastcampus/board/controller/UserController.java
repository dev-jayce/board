package com.fastcampus.board.controller;

import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.user.*;
import com.fastcampus.board.service.PostService;
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

  @GetMapping
  public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String query) {
    var user = userService.getUsers(query);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @GetMapping("/{username}")
  public ResponseEntity<User> getUser(@PathVariable String username) {
    var user = userService.getUser(username);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @GetMapping("/{username}/posts")
  public ResponseEntity<List<Post>> getPostsByUser(@PathVariable String username) {
    var posts = postService.getPostsByUsername(username);
    return new ResponseEntity<>(posts, HttpStatus.OK);
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
