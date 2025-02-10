package com.orion.mdd_api.repositories;

import com.orion.mdd_api.models.Comment;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  Set<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);
}
