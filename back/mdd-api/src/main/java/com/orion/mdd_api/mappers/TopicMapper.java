package com.orion.mdd_api.mappers;

import com.orion.mdd_api.dtos.TopicDto;
import com.orion.mdd_api.models.Topic;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TopicMapper {

  TopicDto toDto(Topic topic);

  List<TopicDto> toDtoList(List<Topic> topics);
}
