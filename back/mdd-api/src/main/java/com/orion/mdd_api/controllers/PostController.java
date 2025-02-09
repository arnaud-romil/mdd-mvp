package com.orion.mdd_api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd_api.dtos.PostDto;
import com.orion.mdd_api.services.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    
    @GetMapping
    public ResponseEntity<List<PostDto>> getUserFeed(Authentication authentication) {
        final String userEmail = authentication.getName();
        List<PostDto> posts = postService.getUserFeed(userEmail);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getSinglePost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.findById(postId));
    }



}
