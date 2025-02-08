package com.orion.mdd_api.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.orion.mdd_api.dtos.CommentDto;
import com.orion.mdd_api.models.Comment;


@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "author.username", target = "author")
    CommentDto toDto(Comment comment);

    List<CommentDto> toDtoList(List<Comment> comments);
}
