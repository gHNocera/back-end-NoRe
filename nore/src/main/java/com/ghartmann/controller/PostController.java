package com.ghartmann.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ghartmann.dao.IUserDAO;
import com.ghartmann.dao.UserDAO;
import com.ghartmann.domain.Post;
import com.ghartmann.domain.User;
import com.ghartmann.dto.PostDTO;
import com.ghartmann.repository.PostRepository;

@RestController
@RequestMapping("/posts")
public class PostController {
    IUserDAO userDAO = new UserDAO();

    @Autowired
    private PostRepository postRepository;

    @PostMapping
    public Post createPost(@RequestBody PostDTO postDTO) {
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setUsername(postDTO.getUsername());
        post.setLikes(postDTO.getLikes());
        post.setComentarios(postDTO.getComments());
        post.setTimeStamp(
        java.time.Instant.ofEpochMilli(postDTO.getTimestamp())
                         .atZone(java.time.ZoneId.systemDefault())
                         .toLocalDateTime()
    );
        return postRepository.save(post);

    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @PostMapping("/{postId}/like/{userId}")
    public Post likePost(@PathVariable Integer postId, @PathVariable Integer userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userDAO.getUserById(userId);

        post.getLikedBy().add(user);
        return postRepository.save(post);
    }


}
