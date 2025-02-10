package com.orion.mdd_api.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DirtiesContext
  @WithMockUser("user2@test.com")
  void shouldAllowUserToSubscribeToTopic() throws Exception {

    mockMvc
        .perform(post("/users/me/topics/2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("user2"))
        .andExpect(jsonPath("$.email").value("user2@test.com"))
        .andExpect(jsonPath("$.topics.length()").value(1))
        .andExpect(jsonPath("$.topics[0].id").value(2))
        .andExpect(jsonPath("$.topics[0].title").value("Spring"))
        .andExpect(
            jsonPath("$.topics[0].description")
                .value(
                    "Spring is a powerful, feature-rich framework for building Java applications."));
  }

  @Test
  @DirtiesContext
  @WithMockUser("user1@test.com")
  void shouldAllowUserToUnsubscribeToTopic() throws Exception {

    mockMvc
        .perform(delete("/users/me/topics/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("user1"))
        .andExpect(jsonPath("$.email").value("user1@test.com"))
        .andExpect(jsonPath("$.topics.length()").value(0));
  }
}
