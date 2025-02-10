package com.orion.mdd_api.services;

import com.orion.mdd_api.dtos.UserDto;
import com.orion.mdd_api.mappers.UserMapper;
import com.orion.mdd_api.models.Topic;
import com.orion.mdd_api.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

  private final UserService userService;
  private final TopicService topicService;
  private final UserMapper userMapper;

  public UserDto addSubscription(String userEmail, Long topicId) {
    User user = userService.findByEmail(userEmail);
    Topic topic = topicService.findById(topicId);
    user.getTopics().add(topic);
    return userMapper.toDto(userService.saveUser(user));
  }

  public UserDto removeSubscription(String userEmail, Long topicId) {
    User user = userService.findByEmail(userEmail);
    Topic topic = topicService.findById(topicId);
    user.getTopics().remove(topic);
    return userMapper.toDto(userService.saveUser(user));
  }
}
