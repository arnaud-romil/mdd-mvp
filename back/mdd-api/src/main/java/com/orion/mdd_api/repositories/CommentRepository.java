package com.orion.mdd_api.repositories;

import com.orion.mdd_api.models.Comment;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository for managing Comment entities */
public interface CommentRepository extends JpaRepository<Comment, Long> {

  /**
   * finds comments by post id and orders them by "created_at" desc
   *
   * @param postId the id of the post containing the comments
   * @return set containing the comments found
   */
  Set<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);
}
