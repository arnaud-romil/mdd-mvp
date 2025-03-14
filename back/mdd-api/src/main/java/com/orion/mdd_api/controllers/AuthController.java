package com.orion.mdd_api.controllers;

import com.orion.mdd_api.dtos.UserDto;
import com.orion.mdd_api.models.User;
import com.orion.mdd_api.payloads.requests.LoginRequest;
import com.orion.mdd_api.payloads.requests.ProfileUpdateRequest;
import com.orion.mdd_api.payloads.requests.RegisterRequest;
import com.orion.mdd_api.payloads.responses.LoginResponse;
import com.orion.mdd_api.payloads.responses.MessageResponse;
import com.orion.mdd_api.services.RefreshTokenService;
import com.orion.mdd_api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller for managing authentication related operations */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(
    name = "Authentication Controller",
    description = "Endpoints for managing authentication features.")
public class AuthController {

  private final UserService userService;
  private final RefreshTokenService refreshTokenService;

  /**
   * Registers a new user to the application
   *
   * @param registerRequest the request to register a new user
   * @return ResponseEntity containing the response message
   */
  @PostMapping("/register")
  @Operation(
      summary = "Registers a new user",
      description = "Creates a new user and returns a message",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "User registered",
            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content())
      })
  public ResponseEntity<MessageResponse> register(
      @Valid @RequestBody RegisterRequest registerRequest) {
    userService.registerUser(registerRequest);
    return ResponseEntity.ok(new MessageResponse("User registered successfully"));
  }

  /**
   * Authenticates a user
   *
   * @param loginRequest the login request containing the user's credentials
   * @param response the http servlet response used to return the refresh token cookie
   * @return LoginResponse containing a JWT token
   */
  @Operation(
      summary = "Authenticates a user",
      description = "Returns JWT Token if the user credentials are valid",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "User is authenticated",
            content = @Content(schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      })
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(
      @Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    LoginResponse loginResponse = userService.login(loginRequest);
    response.addCookie(loginResponse.getRefreshToken());
    return ResponseEntity.ok(loginResponse);
  }

  /**
   * Returns the authenticated user's details
   *
   * @param authentication the authenticated principal
   * @return ResponseEntity containing the user details
   */
  @Operation(
      summary = "Get user details",
      description = "Returns details of the authenticated user",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "User details returned",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      },
      security = @SecurityRequirement(name = "bearerAuth"))
  @GetMapping("/me")
  public ResponseEntity<UserDto> me(Authentication authentication) {
    final String userEmail = authentication.getName();
    UserDto userDto = userService.me(userEmail);
    return ResponseEntity.ok(userDto);
  }

  /**
   * Updates the authenticated user's details and revokes the refresh token
   *
   * @param profileUpdateRequest the request to update the user's details
   * @param authentication the authenticated principal
   * @param response the http servlet response used to return a revoked refresh token cookie
   * @return ResponseEntity containing the user details
   */
  @PutMapping("/me")
  @Operation(
      summary = "Updates user details",
      description = "Updates details of the authenticated user",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "User details updated",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      },
      security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<UserDto> me(
      @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest,
      Authentication authentication,
      HttpServletResponse response) {
    final String userEmail = authentication.getName();
    User user = userService.findByEmail(userEmail);
    Cookie revokedRefreshTokenCookie = refreshTokenService.revokeRefreshToken(user);
    response.addCookie(revokedRefreshTokenCookie);
    UserDto userDto = userService.updateProfile(profileUpdateRequest, userEmail);
    return ResponseEntity.ok(userDto);
  }

  /**
   * Generates a new JWT token given a refresh token cookie
   *
   * @param refreshToken the refresh token cookie
   * @return LoginResponse containing the JWT token
   */
  @PostMapping("/refresh-token")
  @Operation(
      summary = "Refreshes user JWT token",
      description = "Returns JWT Token if the refresh token is valid",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "JWT token has been generated",
            content = @Content(schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      })
  public ResponseEntity<LoginResponse> refreshToken(
      @CookieValue(value = "refreshToken") String refreshToken) {
    LoginResponse loginResponse = refreshTokenService.refreshAccessToken(refreshToken);
    return ResponseEntity.ok(loginResponse);
  }

  /**
   * Logs out an authenticated user
   *
   * @param response the http servlet response used to return a revoked refresh token cookie
   * @param authentication the authenticated principal
   * @return ResponseEntity of Void
   */
  @PostMapping("/logout")
  @Operation(
      summary = "Logs out the authenticated user",
      description = "Revokes the user refresh token",
      responses = {
        @ApiResponse(
            responseCode = "204",
            description = "User is logged out",
            content = @Content()),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      },
      security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<Void> logout(HttpServletResponse response, Authentication authentication) {
    final String userEmail = authentication.getName();
    User user = userService.findByEmail(userEmail);
    Cookie revokedRefreshTokenCookie = refreshTokenService.revokeRefreshToken(user);
    response.addCookie(revokedRefreshTokenCookie);
    return ResponseEntity.noContent().build();
  }
}
