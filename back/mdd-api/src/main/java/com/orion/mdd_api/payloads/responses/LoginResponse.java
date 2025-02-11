package com.orion.mdd_api.payloads.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.servlet.http.Cookie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {

  private String accessToken;

  @JsonIgnore private Cookie refreshToken;

  public LoginResponse(String accessToken) {
    this.accessToken = accessToken;
  }

  public LoginResponse(String accessToken, Cookie refreshToken) {
    this(accessToken);
    this.refreshToken = refreshToken;
  }
}
