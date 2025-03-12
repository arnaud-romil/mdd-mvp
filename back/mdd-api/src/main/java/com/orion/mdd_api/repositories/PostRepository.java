package com.orion.mdd_api.repositories;

import com.orion.mdd_api.models.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository for managing Post entities */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  /**
   * Finds posts by topid id List and orders them by "created_at" desc
   *
   * @param topicIdList List of topic ids
   * @return List containing the posts found
   */
  List<Post> findByTopicIdInOrderByCreatedAtDesc(List<Long> topicIdList);
}
