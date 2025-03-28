package com.orion.mdd_api.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class TopicControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void shouldReturnUnauthorizedStatusCode() throws Exception {
    mockMvc.perform(get("/topics")).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser("user1@test.com")
  void shouldReturnAllTopics() throws Exception {
    mockMvc
        .perform(get("/topics"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(22))
        .andExpect(jsonPath("[0].id").value(1))
        .andExpect(jsonPath("[0].title").value("Java"))
        .andExpect(
            jsonPath("[0].description")
                .value(
                    "Java est un langage de programmation de haut niveau, basé sur les classes et orienté objet."))
        .andExpect(jsonPath("[0].subscribed").value(true))
        .andExpect(jsonPath("[1].id").value(2))
        .andExpect(jsonPath("[1].title").value("Spring"))
        .andExpect(
            jsonPath("[1].description")
                .value(
                    "Spring est un framework puissant et riche en fonctionnalités pour développer des applications Java."))
        .andExpect(jsonPath("[1].subscribed").value(false));
  }
}
