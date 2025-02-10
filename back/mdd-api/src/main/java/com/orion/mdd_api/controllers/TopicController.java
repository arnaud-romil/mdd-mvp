package com.orion.mdd_api.controllers;

import com.orion.mdd_api.dtos.TopicDto;
import com.orion.mdd_api.services.TopicService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

  private final TopicService topicService;

  @GetMapping
  public ResponseEntity<List<TopicDto>> getAllTopics() {
    return ResponseEntity.ok(topicService.getAllTopics());
  }
}
