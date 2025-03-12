package com.orion.mdd_api.controllers;

import com.orion.mdd_api.dtos.UserDto;
import com.orion.mdd_api.services.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller for managing user subscriptions */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Endpoints for managing users")
public class UserController {

  private final SubscriptionService subscriptionService;

  /**
   * Adds a subscription to the requested topic for the authenticated user
   *
   * @param topicId the id of the topic to subscribe to
   * @param authentication the authenticated principal
   * @return ResponseEntity containing the updated user
   */
  @PostMapping("/me/topics/{topicId}")
  @Operation(
      summary = "Adds a subscription to a topic",
      description = "Adds a subscription to a topic for the authenticated user",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Subscription added for the authenticated user",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      },
      security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<UserDto> addSubscription(
      @PathVariable Long topicId, Authentication authentication) {
    final String userEmail = authentication.getName();
    UserDto userDto = subscriptionService.addSubscription(userEmail, topicId);
    return ResponseEntity.ok(userDto);
  }

  /**
   * Removes a subscription to a topic for the authenticated user
   *
   * @param topicId the id of the topic to unsubscribe to
   * @param authentication the authenticated principal
   * @return ResponseEntity containing the updated user
   */
  @DeleteMapping("/me/topics/{topicId}")
  @Operation(
      summary = "Removes a subscription to a topic",
      description = "Removes a subscription to a topic for the authenticated user",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Subscription removed for the authenticated user",
            content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      },
      security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<UserDto> removeSubscription(
      @PathVariable Long topicId, Authentication authentication) {
    final String userEmail = authentication.getName();
    UserDto userDto = subscriptionService.removeSubscription(userEmail, topicId);
    return ResponseEntity.ok(userDto);
  }
}
