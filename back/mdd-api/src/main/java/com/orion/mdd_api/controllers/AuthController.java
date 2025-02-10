package com.orion.mdd_api.controllers;

import com.orion.mdd_api.dtos.UserDto;
import com.orion.mdd_api.payloads.requests.LoginRequest;
import com.orion.mdd_api.payloads.requests.ProfileUpdateRequest;
import com.orion.mdd_api.payloads.requests.RegisterRequest;
import com.orion.mdd_api.payloads.responses.LoginResponse;
import com.orion.mdd_api.payloads.responses.MessageResponse;
import com.orion.mdd_api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<MessageResponse> register(
      @Valid @RequestBody RegisterRequest registerRequest) {
    userService.registerUser(registerRequest);
    return ResponseEntity.ok(new MessageResponse("User registered successfully"));
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    LoginResponse loginResponse = userService.login(loginRequest);
    return ResponseEntity.ok(loginResponse);
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> me(Authentication authentication) {
    final String userEmail = authentication.getName();
    UserDto userDto = userService.me(userEmail);
    return ResponseEntity.ok(userDto);
  }

  @PutMapping("/me")
  public ResponseEntity<UserDto> me(
      @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest,
      Authentication authentication) {
    final String userEmail = authentication.getName();
    UserDto userDto = userService.updateProfile(profileUpdateRequest, userEmail);
    return ResponseEntity.ok(userDto);
  }
}
