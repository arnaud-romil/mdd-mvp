package com.orion.mdd_api.payloads.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Represents a request to register a new user")
public class RegisterRequest {

  @NotEmpty private String username;
  @Email private String email;

  @Size(min = 8, message = "Password must be at least 8 characters long")
  @Pattern(
      regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
      message =
          "Password must contain at least one uppercase letter, one lowercase letter, one digit and one special character")
  private String password;
}
