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
        ;
    }

}
