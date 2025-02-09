package com.orion.mdd_api.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
class PostControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser("user13@test.com")
    void shouldAllowUserToViewPost() throws Exception {
        mockMvc.perform(get("/posts/1"))
        .andExpect(status().isOk())
        .andExpect(content().encoding("UTF-8"))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("Introduction à Java"))
        .andExpect(jsonPath("$.content").value("Java est un langage de programmation populaire utilisé pour le developpement d'applications d'entreprise, mobiles et web."))
        .andExpect(jsonPath("$.topic").value("Java"))
        .andExpect(jsonPath("$.author").value("user1"))
        .andExpect(jsonPath("$.comments.length()").value(1))
        .andExpect(jsonPath("$.comments[0].id").value(1))
        .andExpect(jsonPath("$.comments[0].content").value("Super article, très bien expliqué !"))
        .andExpect(jsonPath("$.comments[0].author").value("user3"))
        .andExpect(jsonPath("$.comments[0].createdAt").exists())
        .andExpect(jsonPath("createdAt").exists())
        ;
    }

    @Test
    @WithMockUser("user18@test.com")
    void shouldAllowUserToViewHisPersonalizedFeed() throws Exception {
        mockMvc.perform(get("/posts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("[0].id").value(1))
        .andExpect(jsonPath("[0].title").value("Introduction à Java"))
        .andExpect(jsonPath("[0].content").value("Java est un langage de programmation populaire utilisé pour le developpement d'applications d'entreprise, mobiles et web."))
        .andExpect(jsonPath("[0].author").value("user1"))
        .andExpect(jsonPath("[0].createdAt").exists())
        .andExpect(jsonPath("[1].id").value(2))
        .andExpect(jsonPath("[1].title").value("Les classes et objets en Java"))
        .andExpect(jsonPath("[1].content").value("Découvrez comment créer et utiliser des classes et objets en Java pour une programmation orientée objet efficace."))
        .andExpect(jsonPath("[1].author").value("user1"))
        .andExpect(jsonPath("[1].createdAt").exists())
        ;
    }

}
