package com.fastcampus.board.config;

import com.fastcampus.board.service.JwtService;
import com.fastcampus.board.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired private JwtService jwtService;
  @Autowired private UserService userService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String BEARER_PREFIX = "Bearer ";
    var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
    var securityContext = SecurityContextHolder.getContext();

    if (!ObjectUtils.isEmpty(authorization)
        && authorization.startsWith(BEARER_PREFIX)
        && securityContext.getAuthentication() == null) {
      var jwtToken = authorization.substring(BEARER_PREFIX.length());
      var username = jwtService.getUsername(jwtToken);
      var userDetails = userService.loadUserByUsername(username);

      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      usernamePasswordAuthenticationToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request));
      securityContext.setAuthentication(usernamePasswordAuthenticationToken);
      SecurityContextHolder.setContext(securityContext);
    }

    filterChain.doFilter(request, response);
  }
}
