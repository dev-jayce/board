package com.fastcampus.board.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtExceptionFilter.class);

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      filterChain.doFilter(request, response);
    } catch (JwtException exception) {
      logger.error("JwtException {}", exception.getMessage());

      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setCharacterEncoding("UTF-8");

      var errorMap = new HashMap<String, Object>();
      errorMap.put("status", HttpStatus.UNAUTHORIZED);
      errorMap.put("message", exception.getMessage());

      ObjectMapper objectMapper = new ObjectMapper();
      String json = objectMapper.writeValueAsString(errorMap);
      response.getWriter().write(json);
    }
  }
}
