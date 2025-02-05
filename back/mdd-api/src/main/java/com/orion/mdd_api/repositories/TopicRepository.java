package com.orion.mdd_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orion.mdd_api.models.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

}
