package com.orion.mdd_api.services;

import org.springframework.stereotype.Service;

import com.orion.mdd_api.dtos.PostDto;
import com.orion.mdd_api.exceptions.InvalidDataException;
import com.orion.mdd_api.mappers.PostMapper;
import com.orion.mdd_api.repositories.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;


    public PostDto findById(Long postId) {
        return postMapper.toDto(
            postRepository.findById(postId)
            .orElseThrow(() -> new InvalidDataException("Post not found"))
        );
    }
}
