package com.fastcampus.board.service;

import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.post.PostPatchRequestBody;
import com.fastcampus.board.model.post.PostPostRequestBody;
import com.fastcampus.board.repository.PostEntityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PostService {

  @Autowired private PostEntityRepository postEntityRepository;

  public List<Post> getPosts() {
    var postEntities = postEntityRepository.findAll();
    return postEntities.stream().map(Post::from).toList();
  }

  public Post getPostByPostId(Long postId) {
    var postEntity =
        postEntityRepository
            .findById(postId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found."));
    return Post.from(postEntity);
  }

  public Post createPost(PostPostRequestBody postPostRequestBody) {
    var postEntity = new PostEntity();
    postEntity.setBody(postPostRequestBody.body());
    var savedPostEntity = postEntityRepository.save(postEntity);
    return Post.from(savedPostEntity);
  }

  public Post updatePost(Long postId, PostPatchRequestBody postPatchRequestBody) {
    var postEntity =
        postEntityRepository
            .findById(postId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found."));
    postEntity.setBody(postPatchRequestBody.body());
    var updatedEntity = postEntityRepository.save(postEntity);
    return Post.from(updatedEntity);
  }

  public void deletePost(Long postId) {
    var postEntity =
        postEntityRepository
            .findById(postId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found."));
    postEntityRepository.delete(postEntity);
  }
}
