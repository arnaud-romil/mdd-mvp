package com.orion.mdd_api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.orion.mdd_api.dtos.TopicDto;
import com.orion.mdd_api.exceptions.InvalidDataException;
import com.orion.mdd_api.mappers.TopicMapper;
import com.orion.mdd_api.models.Topic;
import com.orion.mdd_api.repositories.TopicRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    public List<TopicDto> getAllTopics() {
        return topicMapper.toDtoList(topicRepository.findAll());
    }

    public Topic findById(Long topicId) {
       return topicRepository.findById(topicId)
       .orElseThrow(() -> new InvalidDataException("Topic not found"));
    }

}
