package com.orion.mdd_api.services;

import com.orion.mdd_api.dtos.TopicDto;
import com.orion.mdd_api.exceptions.InvalidDataException;
import com.orion.mdd_api.mappers.TopicMapper;
import com.orion.mdd_api.models.Topic;
import com.orion.mdd_api.repositories.TopicRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicService {

  private final TopicRepository topicRepository;
  private final TopicMapper topicMapper;

  public List<TopicDto> getAllTopics() {
    return topicMapper.toDtoList(topicRepository.findAll());
  }

  public Topic findById(Long topicId) {
    return topicRepository
        .findById(topicId)
        .orElseThrow(() -> new InvalidDataException("Topic not found"));
  }
}
