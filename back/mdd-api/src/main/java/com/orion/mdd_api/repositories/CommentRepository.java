package com.orion.mdd_api.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orion.mdd_api.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Set<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);

}
