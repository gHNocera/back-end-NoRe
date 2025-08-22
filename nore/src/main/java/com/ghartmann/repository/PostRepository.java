package com.ghartmann.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ghartmann.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
