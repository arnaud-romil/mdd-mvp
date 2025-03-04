package com.orion.mdd_api.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Data Transfer Object for Topic. */
@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Data transfer object representing a Topic.")
public class TopicDto {

  private Long id;
  private String title;
  private String description;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Boolean subscribed;
}
