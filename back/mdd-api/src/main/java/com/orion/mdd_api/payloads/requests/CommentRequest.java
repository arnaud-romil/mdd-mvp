package com.orion.mdd_api.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

  @NotBlank private String content;
}
