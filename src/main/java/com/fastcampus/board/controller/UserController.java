package com.fastcampus.board.controller;

import com.fastcampus.board.model.user.User;
import com.fastcampus.board.model.user.UserAuthenticationResponse;
import com.fastcampus.board.model.user.UserLoginRequestBody;
import com.fastcampus.board.model.user.UserSignUpRequestBody;
import com.fastcampus.board.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
  @Autowired UserService userService;

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
