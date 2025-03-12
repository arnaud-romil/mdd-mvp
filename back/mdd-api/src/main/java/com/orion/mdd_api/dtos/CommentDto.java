package com.orion.mdd_api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/** Data Transfer Object for Comment. */
@Getter
@Setter
@Schema(description = "Data transfer object representing a Comment.")
public class CommentDto {

  private Long id;
  private String content;
  private String author;
  private Instant createdAt;
}
