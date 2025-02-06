package com.orion.mdd_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd_api.dtos.UserDto;
import com.orion.mdd_api.services.SubscriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/me/topics/{topicId}")
    public ResponseEntity<UserDto> addSubscription(
        @PathVariable Long topicId,
        Authentication authentication) 
    {
        final String userEmail = authentication.getName();
        UserDto userDto = subscriptionService.addSubscription(userEmail, topicId);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/me/topics/{topicId}")
    public ResponseEntity<UserDto> removeSubscription(
        @PathVariable Long topicId,
        Authentication authentication) 
    {
        final String userEmail = authentication.getName();
        UserDto userDto = subscriptionService.removeSubscription(userEmail, topicId);
        return ResponseEntity.ok(userDto);
    }

}
