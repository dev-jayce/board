package com.fastcampus.board.service;

import com.fastcampus.board.exception.user.UserAlreadyExistsException;
import com.fastcampus.board.exception.user.UserNotFoundException;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.user.User;
import com.fastcampus.board.model.user.UserAuthenticationResponse;
import com.fastcampus.board.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  @Autowired private UserEntityRepository userEntityRepository;

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
}
