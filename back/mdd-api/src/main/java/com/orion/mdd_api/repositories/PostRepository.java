package com.orion.mdd_api.repositories;

import com.orion.mdd_api.models.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  List<Post> findByTopicIdInOrderByCreatedAtDesc(List<Long> topicIdList);
}
