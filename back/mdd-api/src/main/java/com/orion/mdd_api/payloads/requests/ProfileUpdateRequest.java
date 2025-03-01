package com.orion.mdd_api.payloads.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateRequest {

  @NotBlank private String username;
  @Email private String email;

  @Size(min = 8, message = "Password must be at least 8 characters long")
  @Pattern(
      regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
      message =
          "Password must contain at least one uppercase letter, one lowercase letter, one digit and one special character")
  private String password;

  @Size(min = 8, message = "Password must be at least 8 characters long")
  @Pattern(
      regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
      message =
          "Password must contain at least one uppercase letter, one lowercase letter, one digit and one special character")
  private String newPassword;
}
