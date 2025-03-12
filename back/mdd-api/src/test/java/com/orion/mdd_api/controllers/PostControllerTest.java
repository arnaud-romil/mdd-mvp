package com.orion.mdd_api.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class PostControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @WithMockUser("user1@test.com")
  void shouldAllowUserToViewPost() throws Exception {
    mockMvc
        .perform(get("/posts/1"))
        .andExpect(status().isOk())
        .andExpect(content().encoding("UTF-8"))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("Introduction à Java"))
        .andExpect(
            jsonPath("$.content")
                .value(
                    "Java est un langage de programmation populaire utilisé pour le developpement d'applications d'entreprise, mobiles et web."))
        .andExpect(jsonPath("$.topic").value("Java"))
        .andExpect(jsonPath("$.author").value("user1"))
        .andExpect(jsonPath("$.comments.length()").value(1))
        .andExpect(jsonPath("$.comments[0].id").value(1))
        .andExpect(jsonPath("$.comments[0].content").value("Super article, très bien expliqué !"))
        .andExpect(jsonPath("$.comments[0].author").value("user3"))
        .andExpect(jsonPath("$.comments[0].createdAt").exists())
        .andExpect(jsonPath("createdAt").exists());
  }

  @Test
  @WithMockUser("user1@test.com")
  void shouldNotAllowUserToViewPostForAnUnsubscribedTopic() throws Exception {
    mockMvc
        .perform(get("/posts/5"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message").value("User is not subscribed to the topic"));
  }

  @Test
  @WithMockUser("user1@test.com")
  void shouldReturnNotFoundIfThePostDoestNotExist() throws Exception {
    mockMvc
        .perform(get("/posts/99"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Post not found"));
  }

  @Test
  @WithMockUser("user18@test.com")
  void shouldAllowUserToViewHisPersonalizedFeed() throws Exception {
    mockMvc
        .perform(get("/posts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("[0].id").value(1))
        .andExpect(jsonPath("[0].title").value("Introduction à Java"))
        .andExpect(
            jsonPath("[0].content")
                .value(
                    "Java est un langage de programmation populaire utilisé pour le developpement d'applications d'entreprise, mobiles et web."))
        .andExpect(jsonPath("[0].author").value("user1"))
        .andExpect(jsonPath("[0].createdAt").exists())
        .andExpect(jsonPath("[0].comments.length()").value(1))
        .andExpect(jsonPath("[1].id").value(2))
        .andExpect(jsonPath("[1].title").value("Les classes et objets en Java"))
        .andExpect(
            jsonPath("[1].content")
                .value(
                    "Découvrez comment créer et utiliser des classes et objets en Java pour une programmation orientée objet efficace."))
        .andExpect(jsonPath("[1].author").value("user1"))
        .andExpect(jsonPath("[1].createdAt").exists());
  }

  @Test
  @WithMockUser("user18@test.com")
  @DirtiesContext
  void shouldAllowUserToAddCommentToAPost() throws Exception {

    String commentRequest =
        """
                {
                    "content": "Article très utile! Merci."
                }
                """;

    mockMvc
        .perform(
            post("/posts/2/comments")
                .content(commentRequest)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.title").value("Les classes et objets en Java"))
        .andExpect(
            jsonPath("$.content")
                .value(
                    "Découvrez comment créer et utiliser des classes et objets en Java pour une programmation orientée objet efficace."))
        .andExpect(jsonPath("$.comments.length()").value(2))
        .andExpect(jsonPath("$.comments[0].content").value("Article très utile! Merci."))
        .andExpect(jsonPath("$.comments[0].author").value("user18"));
  }

  @Test
  @WithMockUser("user1@test.com")
  @DirtiesContext
  void shouldAllowUserToCreateAPost() throws Exception {

    String postCreationRequest =
        """
                {
                    "title": "Les fondamentaux de Java",
                    "content": "Java est un langage puissant et polyvalent, utilisé pour le développement web, mobile et d’entreprise. Sa portabilité et sa gestion automatique de la mémoire en font un choix populaire.",
                    "topicId": 1
                }
                """;

    mockMvc
        .perform(
            post("/posts").content(postCreationRequest).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Les fondamentaux de Java"))
        .andExpect(
            jsonPath("$.content")
                .value(
                    "Java est un langage puissant et polyvalent, utilisé pour le développement web, mobile et d’entreprise. Sa portabilité et sa gestion automatique de la mémoire en font un choix populaire."))
        .andExpect(jsonPath("$.topic").value("Java"))
        .andExpect(jsonPath("$.author").value("user1"))
        .andExpect(jsonPath("$.comments.length()").value(0))
        .andExpect(jsonPath("$.createdAt").exists());
  }

  @Test
  @WithMockUser("user1@test.com")
  @DirtiesContext
  void shouldNotAllowUserToCreateAPostForAnUnsubscribedTopic() throws Exception {

    String postCreationRequest =
        """
                {
                    "title": "Les fondamentaux de Python",
                    "content": "Python est un langage de programmation puissant et facile à apprendre. Il dispose de structures de données de haut niveau et permet une approche simple mais efficace de la programmation orientée objet.",
                    "topicId": 3
                }
                """;

    mockMvc
        .perform(
            post("/posts").content(postCreationRequest).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message").value("User is not subscribed to the topic"));
  }
}
