package com.fastcampus.board.service;

import com.fastcampus.board.exception.post.PostNotFoundException;
import com.fastcampus.board.exception.reply.ReplyNotFoundException;
import com.fastcampus.board.exception.user.UserNotAllowedException;
import com.fastcampus.board.exception.user.UserNotFoundException;
import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.entity.ReplyEntity;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.reply.Reply;
import com.fastcampus.board.model.reply.ReplyPatchRequestBody;
import com.fastcampus.board.model.reply.ReplyPostRequestBody;
import com.fastcampus.board.repository.PostEntityRepository;
import com.fastcampus.board.repository.ReplyEntityRepository;
import com.fastcampus.board.repository.UserEntityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReplyService {

  @Autowired private PostEntityRepository postEntityRepository;
  @Autowired private UserEntityRepository userEntityRepository;

  @Autowired private ReplyEntityRepository replyEntityRepository;

  public List<Reply> getRepliesByPostId(Long postId) {
    var postEntity =
        postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
    var replyEntities = replyEntityRepository.findByPost(postEntity);
    return replyEntities.stream().map(Reply::from).toList();
  }

  public List<Reply> getRepliesByUser(String username) {
    var user =
        userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

    var replyEntities = replyEntityRepository.findByUser(user);

    return replyEntities.stream().map(Reply::from).toList();
  }

  @Transactional
  public Reply createReply(
      Long postId, ReplyPostRequestBody replyPostRequestBody, UserEntity currentUser) {
    var postEntity =
        postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

    ReplyEntity replyEntity =
        replyEntityRepository.save(
            ReplyEntity.of(replyPostRequestBody.body(), currentUser, postEntity));

    postEntity.setRepliesCount(postEntity.getRepliesCount() + 1);

    return Reply.from(replyEntity);
  }

  public Reply updateReply(
      Long postId,
      Long replyId,
      ReplyPatchRequestBody replyPatchRequestBody,
      UserEntity currentUser) {
    postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
    var replyEntity =
        replyEntityRepository
            .findById(replyId)
            .orElseThrow(() -> new ReplyNotFoundException(replyId));

    if (!replyEntity.getUser().equals(currentUser)) {
      throw new UserNotAllowedException();
    }

    replyEntity.setBody(replyPatchRequestBody.body());
    return Reply.from(replyEntityRepository.save(replyEntity));
  }

  @Transactional
  public void deleteReply(Long postId, Long replyId, UserEntity currentUser) {
    PostEntity postEntity =
        postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
    var replyEntity =
        replyEntityRepository
            .findById(replyId)
            .orElseThrow(() -> new ReplyNotFoundException(replyId));

    if (!replyEntity.getUser().equals(currentUser)) {
      throw new UserNotAllowedException();
    }

    replyEntityRepository.delete(replyEntity);
    postEntity.setRepliesCount(Math.max(0, postEntity.getRepliesCount() - 1));
    postEntityRepository.save(postEntity);
  }
}
