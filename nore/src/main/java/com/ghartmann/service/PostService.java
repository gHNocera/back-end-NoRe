package com.ghartmann.service;

import java.util.List;


import com.ghartmann.dao.IUserDAO;
import com.ghartmann.domain.Post;
import com.ghartmann.domain.User;
import com.ghartmann.dto.PostDTO;
import com.ghartmann.repository.PostRepository;

public class PostService {

    private final PostRepository postRepository;
    private final IUserDAO userDAO;

    public PostService(PostRepository postRepository, IUserDAO userDAO) {
        this.postRepository = postRepository;
        this.userDAO = userDAO;
    }

    public Post createPost(PostDTO postDTO) {
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

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post likePost(Integer postID, Integer userId){
        Post post = postRepository.findById(postID)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userDAO.getUserById(userId);

        // Verificar se o usuário já curtiu o post
        boolean alreadyLiked = post.getLikedBy().stream()
                .anyMatch(likedUser -> likedUser.getId().equals(userId));
        
        if (alreadyLiked) {
            throw new RuntimeException("User already liked this post");
        }

        post.getLikedBy().add(user);
        return postRepository.save(post);
    }
}
