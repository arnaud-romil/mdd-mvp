package com.orion.mdd_api.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Data Transfer Object for Topic. */
@Setter
@Getter
@NoArgsConstructor
public class TopicDto {

  private Long id;
  private String title;
  private String description;
  private boolean subscribed;
}
