package com.orion.mdd_api.controllers;

import com.orion.mdd_api.dtos.PostDto;
import com.orion.mdd_api.payloads.requests.CommentRequest;
import com.orion.mdd_api.payloads.requests.PostCreationRequest;
import com.orion.mdd_api.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Post Controller", description = "Endpoints for managing post features.")
public class PostController {

  private final PostService postService;

  /**
   * Returns the authenticated user's feed
   *
   * @param authentication the authenticated principal
   * @return ResponseEntity containing the list of posts for the authenticated user
   */
  @GetMapping
  @Operation(
      summary = "Get user feed",
      description = "Returns the feed of posts for the authenticated user",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "User feed returned",
            content = @Content(schema = @Schema(implementation = PostDto[].class))),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      },
      security = @SecurityRequirement(name = "bearerAuth"))
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
  @Operation(
      summary = "Returns a single post",
      description = "Finds and returns a single post by ID",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Post successfully returned",
            content = @Content(schema = @Schema(implementation = PostDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      },
      security = @SecurityRequirement(name = "bearerAuth"))
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
  @Operation(
      summary = "Adds a comment to a post",
      description = "Adds a comment to the post identified by ID",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Comment successfully added",
            content = @Content(schema = @Schema(implementation = PostDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      },
      security = @SecurityRequirement(name = "bearerAuth"))
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
  @Operation(
      summary = "Creates a new post",
      description = "Creates a new post and returns it",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Post created",
            content = @Content(schema = @Schema(implementation = PostDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "User is unauthorized",
            content = @Content())
      },
      security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<PostDto> createPost(
      @Valid @RequestBody PostCreationRequest postCreationRequest, Authentication authentication) {
    final String userEmail = authentication.getName();
    PostDto post = postService.createPost(postCreationRequest, userEmail);
    return ResponseEntity.ok(post);
  }
}
