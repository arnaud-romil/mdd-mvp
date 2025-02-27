package com.orion.mdd_api.services;

import com.orion.mdd_api.dtos.PostDto;
import com.orion.mdd_api.exceptions.InvalidDataException;
import com.orion.mdd_api.mappers.PostMapper;
import com.orion.mdd_api.models.Comment;
import com.orion.mdd_api.models.Post;
import com.orion.mdd_api.models.Topic;
import com.orion.mdd_api.models.User;
import com.orion.mdd_api.payloads.requests.CommentRequest;
import com.orion.mdd_api.payloads.requests.PostCreationRequest;
import com.orion.mdd_api.repositories.PostRepository;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Service for handling post-related operations. */
@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final PostMapper postMapper;
  private final UserService userService;
  private final CommentService commentService;
  private final TopicService topicService;

  /**
   * Finds a post by ID.
   *
   * @param postId the id of the post
   * @return the post
   */
  public PostDto getSinglePost(Long postId) {
    return postMapper.toDto(findById(postId));
  }

  /**
   * Returns the authenticated user's feed
   *
   * @param userEmail the email adress of the user
   * @return List containing the posts for the user
   */
  public List<PostDto> getUserFeed(String userEmail) {
    User user = userService.findByEmail(userEmail);
    List<Long> topicIdList = user.getTopics().stream().map(Topic::getId).toList();
    List<Post> posts = postRepository.findByTopicIdInOrderByCreatedAtDesc(topicIdList);

    return postMapper.toDtoList(posts);
  }

  /**
   * Adds a comment to a post
   *
   * @param postId the post to add the comment to
   * @param commentRequest the comment to add
   * @param userEmail the authenticated user's email adress
   * @return the updated post
   */
  public PostDto addComment(Long postId, CommentRequest commentRequest, String userEmail) {
    Post post = findById(postId);
    User user = userService.findByEmail(userEmail);

    Comment comment = new Comment();
    comment.setContent(commentRequest.getContent());
    comment.setPost(post);
    comment.setAuthor(user);
    comment.setCreatedAt(Instant.now());

    commentService.save(comment);

    post.setComments(commentService.findByPostIdOrderByCreatedAtDesc(postId));

    return postMapper.toDto(findById(postId));
  }

  private Post findById(Long postId) {
    return postRepository
        .findById(postId)
        .orElseThrow(() -> new InvalidDataException("Post not found"));
  }

  /**
   * Creates a new post
   *
   * @param postCreationRequest the request to create a new post
   * @param userEmail the authenticated user's email
   * @return the created post
   */
  public PostDto createPost(PostCreationRequest postCreationRequest, String userEmail) {
    User user = userService.findByEmail(userEmail);

    Topic topic = topicService.findById(postCreationRequest.getTopicId());

    Post post = new Post();
    post.setTitle(postCreationRequest.getTitle());
    post.setContent(postCreationRequest.getContent());
    post.setTopic(topic);
    post.setAuthor(user);
    post.setComments(Collections.emptySet());
    post.setCreatedAt(Instant.now());

    postRepository.save(post);

    return postMapper.toDto(post);
  }
}
