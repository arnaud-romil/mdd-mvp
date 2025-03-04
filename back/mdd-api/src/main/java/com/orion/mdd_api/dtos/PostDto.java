package com.orion.mdd_api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Data Transfer Object for Post. */
@Getter
@Setter
@Schema(description = "Data transfer object representing a Post.")
public class PostDto {

  private Long id;
  private String title;
  private String content;
  private String topic;
  private String author;
  private List<CommentDto> comments;
  private Instant createdAt;
}
