package com.orion.mdd_api.services;

import com.orion.mdd_api.models.Comment;
import com.orion.mdd_api.repositories.CommentRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;

  public void save(Comment comment) {
    commentRepository.save(comment);
  }

  public Set<Comment> findByPostIdOrderByCreatedAtDesc(Long postId) {
    return commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
  }
}
