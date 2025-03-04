package com.orion.mdd_api.payloads.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Represents a login request.")
public class LoginRequest {

  @NotBlank private String login;

  @NotBlank private String password;
}
