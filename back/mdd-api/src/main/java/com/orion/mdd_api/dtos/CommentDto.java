package com.orion.mdd_api.dtos;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

  private Long id;
  private String content;
  private String author;
  private Instant createdAt;
}
