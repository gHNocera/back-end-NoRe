package com.ghartmann.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ghartmann.domain.Post;
import com.ghartmann.domain.User;
import com.ghartmann.dto.PostDTO;
import com.ghartmann.dto.PostLikeDTO;
import com.ghartmann.repository.PostRepository;
import com.ghartmann.repository.UserRepository;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    /** Criar novo post */
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

    /** Listar todos os posts */
    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    /** Curtir post */
    @PostMapping("/like")
    public Post likePost(@RequestBody PostLikeDTO dto) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!post.getLikedBy().contains(user)) {
            post.getLikedBy().add(user);
            post.setLikes(post.getLikes() + 1);
            postRepository.save(post);
        }

    return post;
}

}
