package com.fastcampus.board.service;

import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.post.PostPostRequestBody;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private static final List<Post> posts = new ArrayList<>();

    static {
        posts.add(new Post(1L, "Post 1", ZonedDateTime.now()));
        posts.add(new Post(2L, "Post 2", ZonedDateTime.now()));
        posts.add(new Post(3L, "Post 3", ZonedDateTime.now()));
    }

    public List<Post> getPosts() {
        return posts;
    }

    public Optional<Post> getPostById(Long postId) {
        return posts.stream()
                .filter(post -> postId.equals(post.postId()))
                .findFirst();
    }

    public Post createPost(PostPostRequestBody postPostRequestBody) {
        var newPostId = posts.stream()
                .mapToLong(Post::postId)
                .max()
                .orElse(0L) + 1;

        var newPost = new Post(newPostId, postPostRequestBody.body(), ZonedDateTime.now());
        posts.add(newPost);
        return newPost;
    }
}