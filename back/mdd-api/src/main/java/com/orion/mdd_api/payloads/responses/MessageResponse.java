package com.orion.mdd_api.payloads.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Schema(description = "Represents a message response.")
public class MessageResponse {

  private final String message;
}
