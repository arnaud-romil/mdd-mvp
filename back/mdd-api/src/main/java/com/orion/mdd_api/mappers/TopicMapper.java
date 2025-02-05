package com.orion.mdd_api.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.orion.mdd_api.dtos.responses.TopicDto;
import com.orion.mdd_api.models.Topic;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    TopicDto toDto(Topic topic);

    List<TopicDto> toDtoList(List<Topic> topics);

}
