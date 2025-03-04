package com.orion.mdd_api.payloads.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Represents a request to create a new post")
public class PostCreationRequest {

  @NotBlank private String title;
  @NotBlank private String content;
  @NotNull private Long topicId;
}
