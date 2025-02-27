package com.orion.mdd_api.services;

import com.orion.mdd_api.models.Comment;
import com.orion.mdd_api.repositories.CommentRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Service for handling comment-related operations */
@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;

  /**
   * Saves a comment
   *
   * @param comment the comment to save
   */
  public void save(Comment comment) {
    commentRepository.save(comment);
  }

  /**
   * finds comments by post id and orders them by "created_at" desc
   *
   * @param postId the id of the post containig the comments
   * @return Set containing the comments
   */
  public Set<Comment> findByPostIdOrderByCreatedAtDesc(Long postId) {
    return commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
  }
}
