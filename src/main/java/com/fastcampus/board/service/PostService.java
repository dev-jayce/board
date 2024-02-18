package com.fastcampus.board.service;

import com.fastcampus.board.exception.post.PostNotFoundException;
import com.fastcampus.board.exception.user.UserNotAllowedException;
import com.fastcampus.board.exception.user.UserNotFoundException;
import com.fastcampus.board.model.entity.LikeEntity;
import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.post.PostPatchRequestBody;
import com.fastcampus.board.model.post.PostPostRequestBody;
import com.fastcampus.board.repository.LikeEntityRepository;
import com.fastcampus.board.repository.PostEntityRepository;
import com.fastcampus.board.repository.UserEntityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

  @Autowired private PostEntityRepository postEntityRepository;
  @Autowired private UserEntityRepository userEntityRepository;

  @Autowired private LikeEntityRepository likeEntityRepository;

  public List<Post> getPosts(UserEntity currentUser) {
    var projections = postEntityRepository.findPostsWithLikingStatus(currentUser.getUserId());
    return projections.stream().map(Post::from).toList();
  }

  public List<Post> getPostsByUsername(String username, UserEntity currentUser) {
    var user =
        userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    var projections =
        postEntityRepository.findPostsByUserIdWithLikingStatus(
            user.getUserId(), currentUser.getUserId());
    return projections.stream().map(Post::from).toList();
  }

  public Post getPostByPostId(Long postId, UserEntity currentUser) {
    var postEntity =
        postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
    return getPostWithLikingStatus(postEntity, currentUser);
  }

  public Post createPost(PostPostRequestBody postPostRequestBody, UserEntity currentUser) {
    var savedPostEntity =
        postEntityRepository.save(PostEntity.of(postPostRequestBody.body(), currentUser));
    return Post.from(savedPostEntity);
  }

  public Post updatePost(
      Long postId, PostPatchRequestBody postPatchRequestBody, UserEntity currentUser) {
    var postEntity =
        postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

    if (!postEntity.getUser().equals(currentUser)) {
      throw new UserNotAllowedException();
    }

    postEntity.setBody(postPatchRequestBody.body());
    var updatedEntity = postEntityRepository.save(postEntity);
    return Post.from(updatedEntity);
  }

  public void deletePost(Long postId, UserEntity currentUser) {
    var postEntity =
        postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

    if (!postEntity.getUser().equals(currentUser)) {
      throw new UserNotAllowedException();
    }

    postEntityRepository.delete(postEntity);
  }

  @Transactional
  public Post toggleLike(Long postId, UserEntity currentUser) {
    PostEntity postEntity =
        postEntityRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    var likeEntity = likeEntityRepository.findByUserAndPost(currentUser, postEntity);

    if (likeEntity.isPresent()) {
      likeEntityRepository.delete(likeEntity.get());
      postEntity.setLikesCount(Math.max(0, postEntity.getLikesCount() - 1));
      return Post.from(postEntityRepository.save(postEntity), false);
    } else {
      likeEntityRepository.save(LikeEntity.of(currentUser, postEntity));
      postEntity.setLikesCount(postEntity.getLikesCount() + 1);
      return Post.from(postEntityRepository.save(postEntity), true);
    }
  }

  private Post getPostWithLikingStatus(PostEntity postEntity, UserEntity currentUser) {
    var isLiking = likeEntityRepository.findByUserAndPost(currentUser, postEntity).isPresent();
    return Post.from(postEntity, isLiking);
  }
}
