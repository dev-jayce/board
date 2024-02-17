package com.fastcampus.board.service;

import com.fastcampus.board.exception.post.PostNotFoundException;
import com.fastcampus.board.exception.user.UserNotAllowedException;
import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.post.PostPatchRequestBody;
import com.fastcampus.board.model.post.PostPostRequestBody;
import com.fastcampus.board.repository.PostEntityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

  @Autowired private PostEntityRepository postEntityRepository;

  public List<Post> getPosts() {
    var postEntities = postEntityRepository.findAll();
    return postEntities.stream().map(Post::from).toList();
  }

  public Post getPostByPostId(Long postId) {
    var postEntity =
        postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
    return Post.from(postEntity);
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
}
