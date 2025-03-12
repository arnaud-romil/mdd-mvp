package com.orion.mdd_api.repositories;

import com.orion.mdd_api.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository for managing Topic entities */
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {}
