package com.orion.mdd_api.services;

import com.orion.mdd_api.dtos.TopicDto;
import com.orion.mdd_api.exceptions.InvalidDataException;
import com.orion.mdd_api.mappers.TopicMapper;
import com.orion.mdd_api.models.Topic;
import com.orion.mdd_api.models.User;
import com.orion.mdd_api.repositories.TopicRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Service for handling topic-related operations. */
@Service
@RequiredArgsConstructor
public class TopicService {

  private final TopicRepository topicRepository;
  private final TopicMapper topicMapper;
  private final UserService userService;

  /**
   * Retrieves all topics
   *
   * @param userEmail the authenticated user's email address
   * @return List containing all topics
   */
  public List<TopicDto> getAllTopics(String userEmail) {
    User user = userService.findByEmail(userEmail);
    List<Topic> topics = topicRepository.findAll();
    topics.forEach(
        topic -> {
          if (user.getTopics().contains(topic)) {
            topic.setSubscribed(true);
          }
        });
    return topicMapper.toDtoList(topics);
  }

  /**
   * Retrieves a topic by ID
   *
   * @param topicId the id of the topic to retrieve
   * @return the topic
   */
  public Topic findById(Long topicId) {
    return topicRepository
        .findById(topicId)
        .orElseThrow(() -> new InvalidDataException("Topic not found"));
  }
}
