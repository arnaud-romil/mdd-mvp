package com.orion.mdd_api.services;

import com.orion.mdd_api.dtos.UserDto;
import com.orion.mdd_api.mappers.UserMapper;
import com.orion.mdd_api.models.Topic;
import com.orion.mdd_api.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Service for handling subscriptions-related operations. */
@Service
@RequiredArgsConstructor
public class SubscriptionService {

  private final UserService userService;
  private final TopicService topicService;
  private final UserMapper userMapper;

  /**
   * Adds a subscription to a given topic for the authenticated user
   *
   * @param userEmail the authenticated user's email address
   * @param topicId the id of the topic to subscribe to
   * @return UserDto containing the updated user
   */
  public UserDto addSubscription(String userEmail, Long topicId) {
    User user = userService.findByEmail(userEmail);
    Topic topic = topicService.findById(topicId);
    user.getTopics().add(topic);
    return userMapper.toDto(userService.saveUser(user));
  }

  /**
   * Removes a subscription to a given topic for the authenticated user
   *
   * @param userEmail the authenticated user's email address
   * @param topicId the id of the topic to unsubscribe to
   * @return UserDto containing the updated user
   */
  public UserDto removeSubscription(String userEmail, Long topicId) {
    User user = userService.findByEmail(userEmail);
    Topic topic = topicService.findById(topicId);
    user.getTopics().remove(topic);
    return userMapper.toDto(userService.saveUser(user));
  }
}
