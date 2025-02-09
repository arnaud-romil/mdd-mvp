package com.orion.mdd_api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.orion.mdd_api.dtos.PostDto;
import com.orion.mdd_api.exceptions.InvalidDataException;
import com.orion.mdd_api.mappers.PostMapper;
import com.orion.mdd_api.models.Post;
import com.orion.mdd_api.models.Topic;
import com.orion.mdd_api.models.User;
import com.orion.mdd_api.repositories.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserService userService;

    public PostDto findById(Long postId) {
        return postMapper.toDto(
            postRepository.findById(postId)
            .orElseThrow(() -> new InvalidDataException("Post not found"))
        );
    }

    public List<PostDto> getUserFeed(String userEmail) {
        User user = userService.findByEmail(userEmail);
        List<Long> topicIdList = user.getTopics().stream().map(Topic::getId).toList();
        List<Post> posts = postRepository.findByTopicIdInOrderByCreatedAtDesc(topicIdList);
        return postMapper.toDtoList(posts);        
    }
}
