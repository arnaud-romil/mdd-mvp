package com.orion.mdd_api.dtos;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private String topic;
    private String author;
    private List<CommentDto> comments;    
    private Instant createdAt;
}
