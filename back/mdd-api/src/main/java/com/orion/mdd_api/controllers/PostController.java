package com.orion.mdd_api.controllers;

import com.orion.mdd_api.dtos.PostDto;
import com.orion.mdd_api.payloads.requests.CommentRequest;
import com.orion.mdd_api.payloads.requests.PostCreationRequest;
import com.orion.mdd_api.services.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller for managing posts */
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  /**
   * Returns the authenticated user's feed
   *
   * @param authentication the authenticated principal
   * @return ResponseEntity containing the list of posts for the authenticated user
   */
  @GetMapping
  public ResponseEntity<List<PostDto>> getUserFeed(Authentication authentication) {
    final String userEmail = authentication.getName();
    List<PostDto> posts = postService.getUserFeed(userEmail);
    return ResponseEntity.ok(posts);
  }

  /**
   * Retrieves a post by it's id
   *
   * @param postId the id of the post to retrieve
   * @return ResponseEntity containing the post
   */
  @GetMapping("/{postId}")
  public ResponseEntity<PostDto> getSinglePost(
      @PathVariable Long postId, Authentication authentication) {
    final String userEmail = authentication.getName();
    return ResponseEntity.ok(postService.getSinglePost(postId, userEmail));
  }

  /**
   * Adds a comment to a post
   *
   * @param postId the id of the post to add the comment to
   * @param commentRequest the comment to add to the post
   * @param authentication the authenticated principal (the comment's author)
   * @return ResponseEntity containig the updated post
   */
  @PostMapping("/{postId}/comments")
  public ResponseEntity<PostDto> addComment(
      @PathVariable Long postId,
      @Valid @RequestBody CommentRequest commentRequest,
      Authentication authentication) {
    final String userEmail = authentication.getName();
    PostDto post = postService.addComment(postId, commentRequest, userEmail);
    return ResponseEntity.ok(post);
  }

  /**
   * Creates a new post
   *
   * @param postCreationRequest the request to create a new post
   * @param authentication the authenticated principal
   * @return ResponseEntity containing the created post
   */
  @PostMapping
  public ResponseEntity<PostDto> createPost(
      @Valid @RequestBody PostCreationRequest postCreationRequest, Authentication authentication) {
    final String userEmail = authentication.getName();
    PostDto post = postService.createPost(postCreationRequest, userEmail);
    return ResponseEntity.ok(post);
  }
}
