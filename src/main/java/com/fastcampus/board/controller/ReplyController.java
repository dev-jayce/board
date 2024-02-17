package com.fastcampus.board.controller;

import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.reply.Reply;
import com.fastcampus.board.model.reply.ReplyPatchRequestBody;
import com.fastcampus.board.model.reply.ReplyPostRequestBody;
import com.fastcampus.board.service.ReplyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts/{postId}")
public class ReplyController {
  @Autowired private ReplyService replyService;

  @GetMapping("/replies")
  public ResponseEntity<List<Reply>> getRepliesByPostId(@PathVariable Long postId) {
    var replies = replyService.getRepliesByPostId(postId);
    return ResponseEntity.ok(replies);
  }

  @PostMapping("/replies")
  public ResponseEntity<Reply> createReply(
      @PathVariable Long postId,
      @RequestBody ReplyPostRequestBody replyPostRequestBody,
      Authentication authentication) {
    var reply =
        replyService.createReply(
            postId, replyPostRequestBody, (UserEntity) authentication.getPrincipal());
    return ResponseEntity.ok(reply);
  }

  @PatchMapping("/replies/{replyId}")
  public ResponseEntity<Reply> updatePost(
      @PathVariable Long postId,
      @PathVariable Long replyId,
      @RequestBody ReplyPatchRequestBody replyPatchRequestBody,
      Authentication authentication) {
    var reply =
        replyService.updateReply(
            postId, replyId, replyPatchRequestBody, (UserEntity) authentication.getPrincipal());
    return ResponseEntity.ok(reply);
  }

  @DeleteMapping("/replies/{replyId}")
  public ResponseEntity<Void> deleteReply(
      @PathVariable Long postId, @PathVariable Long replyId, Authentication authentication) {
    replyService.deleteReply(postId, replyId, (UserEntity) authentication.getPrincipal());
    return ResponseEntity.noContent().build();
  }
}
