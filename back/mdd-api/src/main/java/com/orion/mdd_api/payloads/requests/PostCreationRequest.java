package com.orion.mdd_api.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreationRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private Long topicId;
}
