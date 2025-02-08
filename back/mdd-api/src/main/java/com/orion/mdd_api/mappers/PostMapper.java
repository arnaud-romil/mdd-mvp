package com.orion.mdd_api.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.orion.mdd_api.dtos.PostDto;
import com.orion.mdd_api.models.Post;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface PostMapper {

    @Mapping(source = "author.username", target = "author")
    @Mapping(source = "topic.title", target = "topic")
    @Mapping(source = "comments", target = "comments")
    PostDto toDto(Post post);

    List<PostDto> toDtoList(List<Post> posts);
}
