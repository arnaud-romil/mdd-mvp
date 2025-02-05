package com.orion.mdd_api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.orion.mdd_api.dtos.responses.TopicDto;
import com.orion.mdd_api.mappers.TopicMapper;
import com.orion.mdd_api.repositories.TopicRepository;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    public TopicService(TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    public List<TopicDto> getAllTopics() {
        return topicMapper.toDtoList(topicRepository.findAll());
    }

}
