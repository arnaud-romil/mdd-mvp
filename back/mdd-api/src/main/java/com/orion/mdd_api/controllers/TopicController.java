package com.orion.mdd_api.controllers;

import com.orion.mdd_api.dtos.TopicDto;
import com.orion.mdd_api.services.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller for managing topics */
@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
@Tag(name = "Topic Controller", description = "Endpoints for managing topics")
public class TopicController {

  private final TopicService topicService;

  /**
   * Retrieves all topics
   *
   * @param authentication the authenticated principal
   * @return ResponseEntity containig a list of all topics
   */
  @GetMapping
  @Operation(
      summary = "Get all topics",
      description = "Retrieves all topics",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "All topics returned",
            content = @Content(schema = @Schema(implementation = TopicDto[].class))),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      },
      security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<List<TopicDto>> getAllTopics(Authentication authentication) {
    final String userEmail = authentication.getName();
    return ResponseEntity.ok(topicService.getAllTopics(userEmail));
  }
}
