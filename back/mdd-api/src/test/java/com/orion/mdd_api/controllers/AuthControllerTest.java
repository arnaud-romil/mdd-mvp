package com.orion.mdd_api.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orion.mdd_api.models.User;
import com.orion.mdd_api.payloads.responses.LoginResponse;
import com.orion.mdd_api.repositories.RefreshTokenRepository;
import com.orion.mdd_api.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import java.time.Instant;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private UserRepository userRepository;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private RefreshTokenRepository refreshTokenRepository;

  @Test
  @DirtiesContext
  void shouldAllowUserToRegister() throws Exception {

    String registerRequest =
        """
                {
                    "username": "user35",
                    "email": "user35@test.com",
                    "password": "user35Password!"
                }
                """;

    mockMvc
        .perform(
            post("/auth/register").content(registerRequest).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("User registered successfully"));

    User user = userRepository.findByUsername("user35").orElse(null);
    assertNotNull(user);
    assertEquals("user35", user.getUsername());
    assertEquals("user35@test.com", user.getEmail());
    assertNotEquals("user35Password!", user.getPassword());
  }

  @ParameterizedTest
  @MethodSource("provideRegisterRequests")
  void shoulNotAllowUserToRegister_WhenEmailOrUsernameIsAlreadyTaken(ArgumentsAccessor arguments)
      throws Exception {

    int index = arguments.getInteger(0);
    String registerRequest = arguments.getString(1);

    String expectedValue = index == 0 ? "Email is already taken" : "Username is already taken";

    mockMvc
        .perform(
            post("/auth/register").content(registerRequest).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(expectedValue));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        // Username is empty
        """
                        {
                            "username": "",
                            "email": "valid@email.com",
                            "password": "user1Password!"
                        }
                    """,
        // Password has no digit
        """
                        {
                            "username": "user1",
                            "email": "valid@email.com",
                            "password": "userPassword!"
                        }
                    """,
        // Password has no special character
        """
                        {
                            "username": "user1",
                            "email": "valid@email.com",
                            "password": "user1Password"
                        }
                    """,
        // Password has no lower case letter
        """
                        {
                            "username": "user1",
                            "email": "valid@email.com",
                            "password": "USER1PASSWORD!"
                        }
                    """,
        // Password has no uppercase letter
        """
                        {
                            "username": "user1",
                            "email": "valid@email.com",
                            "password": "user1password!"
                        }
                    """,
        // Password is too short
        """
                        {
                            "username": "user1",
                            "email": "valid@email.com",
                            "password": "u1P!"
                        }
                    """,
        // Email is invalid
        """
                        {
                            "username": "user1",
                            "email": "invalid-email.com",
                            "password": "user1Password!"
                        }
                    """
      })
  void shouldNotAllowUserToRegister_WhenRegisterRequestIsInvalid(String registerRequest)
      throws Exception {

    mockMvc
        .perform(
            post("/auth/register").content(registerRequest).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  static Stream<Arguments> provideRegisterRequests() {

    final String emailAlreadyTaken =
        """
                    {
                        "username": "user99",
                        "email": "user1@test.com",
                        "password": "user99Password!"
                    }
                """;

    final String usernameAlreadyTaken =
        """
                    {
                        "username": "user2",
                        "email": "user99@test.com",
                        "password": "user99Password!"
                    }
                """;

    return Stream.of(Arguments.of(0, emailAlreadyTaken), Arguments.of(1, usernameAlreadyTaken));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        // Login with username
        """
                        {
                            "login": "user1",
                            "password": "user1Password!"
                        }
                    """,
        // Login with email
        """
                        {
                            "login": "user1@test.com",
                            "password": "user1Password!"
                        }
                    """
      })
  void shouldAllowUserToLogin(String loginRequest) throws Exception {

    MvcResult result =
        mockMvc
            .perform(
                post("/auth/login").content(loginRequest).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(cookie().exists("refreshToken"))
            .andExpect(cookie().httpOnly("refreshToken", true))
            .andReturn();

    LoginResponse loginResponse =
        objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class);

    mockMvc
        .perform(get("/topics").header("Authorization", "Bearer " + loginResponse.getAccessToken()))
        .andExpect(status().isOk());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        // Invalid login
        """
                        {
                            "login": "invalid-login",
                            "password": "user1Password!"
                        }
                    """,
        // Invalid password
        """
                        {
                            "login": "user1@test.com",
                            "password": "invalid-password"
                        }
                    """
      })
  void shouldNotAllowUserToLoginWithBadCredentials(String badCredentials) throws Exception {

    mockMvc
        .perform(
            post("/auth/login").content(badCredentials).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("Invalid credentials"));
  }

  @Test
  @WithMockUser("user1@test.com")
  void shouldAllowUserToAccessProfile() throws Exception {

    mockMvc
        .perform(get("/auth/me"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("user1"))
        .andExpect(jsonPath("$.email").value("user1@test.com"))
        .andExpect(jsonPath("$.topics.length()").value(1))
        .andExpect(jsonPath("$.topics[0].id").value(1))
        .andExpect(jsonPath("$.topics[0].title").value("Java"))
        .andExpect(
            jsonPath("$.topics[0].description")
                .value(
                    "Java est un langage de programmation de haut niveau, basé sur les classes et orienté objet."));
  }

  @DirtiesContext
  @WithMockUser("user1@test.com")
  @ParameterizedTest
  @MethodSource("provideProfileUpdateRequests")
  void shouldAllowUserToUpdateProfile(ArgumentsAccessor arguments) throws Exception {

    int index = arguments.getInteger(0);
    String profileUpdateRequest = arguments.getString(1);

    String expectedUsername;
    String expectedEmail;

    if (index == 0) {
      expectedUsername = "new-username";
      expectedEmail = "user1@test.com";
    } else if (index == 1) {
      expectedUsername = "user1";
      expectedEmail = "new-email@test.com";
    } else {
      expectedUsername = "user1";
      expectedEmail = "user1@test.com";
    }

    mockMvc
        .perform(
            put("/auth/me").content(profileUpdateRequest).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(expectedUsername))
        .andExpect(jsonPath("$.email").value(expectedEmail))
        .andExpect(jsonPath("$.topics.length()").value(1))
        .andExpect(jsonPath("$.topics[0].id").value(1))
        .andExpect(jsonPath("$.topics[0].title").value("Java"))
        .andExpect(
            jsonPath("$.topics[0].description")
                .value(
                    "Java est un langage de programmation de haut niveau, basé sur les classes et orienté objet."));
  }

  static Stream<Arguments> provideProfileUpdateRequests() {

    final String updateUsername =
        """
                    {
                        "username": "new-username",
                        "email": "user1@test.com"
                    }
                """;

    final String updateEmail =
        """
                    {
                        "username": "user1",
                        "email": "new-email@test.com"
                    }
                """;

    final String updatePassword =
        """
                    {
                        "username": "user1",
                        "email": "user1@test.com",
                        "password": "user1Password!",
                        "newPassword": "newPassword!123"
                    }
                """;

    return Stream.of(
        Arguments.of(0, updateUsername),
        Arguments.of(1, updateEmail),
        Arguments.of(2, updatePassword));
  }

  @Test
  void shouldAllowUserToRefreshAccessToken() throws Exception {

    Cookie refreshTokenCookie = new Cookie("refreshToken", "333c2b10-d887-4298-abdf-9e8fdef01ca3");

    MvcResult result =
        mockMvc
            .perform(
                post("/auth/refresh-token")
                    .cookie(refreshTokenCookie)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("accessToken").exists())
            .andReturn();

    LoginResponse loginResponse =
        objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class);

    mockMvc
        .perform(
            get("/auth/me").header("Authorization", "Bearer " + loginResponse.getAccessToken()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("user1"));
  }

  @Test
  void shouldNotAllowUserToGetTokenWithInvalidRefreshTokenCookie() throws Exception {

    Cookie refreshTokenCookie = new Cookie("refreshToken", "invalid-refresh-token");

    mockMvc
        .perform(
            post("/auth/refresh-token")
                .cookie(refreshTokenCookie)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DirtiesContext
  @WithMockUser("user1@test.com")
  void shouldAllowUserToLogOut() throws Exception {

    mockMvc.perform(post("/auth/logout")).andExpect(status().isNoContent());

    assertFalse(
        refreshTokenRepository
            .findByTokenAndExpiresAtAfter("333c2b10-d887-4298-abdf-9e8fdef01ca3", Instant.now())
            .isPresent());
  }
}
