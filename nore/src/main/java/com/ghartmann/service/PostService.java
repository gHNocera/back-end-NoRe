package com.ghartmann.service;

import com.ghartmann.domain.Post;
import com.ghartmann.domain.User;
import com.ghartmann.repository.PostRepository;
import com.ghartmann.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    /** Criar novo post */
    @Transactional
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    /** Buscar post por ID */
    public Optional<Post> getPostById(Integer postId) {
        return postRepository.findById(postId);
    }

    /** Curtir post */
    @Transactional
    public void likePost(Integer postId, Integer userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Evita likes duplicados
        if (!post.getLikedBy().contains(user)) {
            post.getLikedBy().add(user);
            post.setLikes(post.getLikes() + 1);
            postRepository.save(post);
        }
    }

    /** Descurtir post */
    @Transactional
    public void unlikePost(Integer postId, Integer userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (post.getLikedBy().remove(user)) {
            post.setLikes(post.getLikes() - 1);
            postRepository.save(post);
        }
    }

    /** Deletar post */
    @Transactional
    public void deletePost(Integer postId) {
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("Post não encontrado");
        }
        postRepository.deleteById(postId);
    }

    /** Atualizar post */
    @Transactional
    public Post updatePost(Integer postId, String newContent) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        post.setContent(newContent);
        return postRepository.save(post);
    }

    /** Fazer comentário */
    @Transactional
    public void addComment(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        post.setComentarios(post.getComentarios() + 1);
        postRepository.save(post);
    }

    /** Remover comentário */
    @Transactional
    public void removeComment(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        if (post.getComentarios() > 0) {
            post.setComentarios(post.getComentarios() - 1);
            postRepository.save(post);
        }
    }
}
