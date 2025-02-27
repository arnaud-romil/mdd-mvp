package com.orion.mdd_api.controllers;

import com.orion.mdd_api.dtos.TopicDto;
import com.orion.mdd_api.services.TopicService;
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
public class TopicController {

  private final TopicService topicService;

  /**
   * Retrieves all topics
   *
   * @param authentication the authenticated principal
   * @return ResponseEntity containig a list of all topics
   */
  @GetMapping
  public ResponseEntity<List<TopicDto>> getAllTopics(Authentication authentication) {
    final String userEmail = authentication.getName();
    return ResponseEntity.ok(topicService.getAllTopics(userEmail));
  }
}
