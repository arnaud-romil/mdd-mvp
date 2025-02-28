package com.orion.mdd_api.services;

import com.orion.mdd_api.dtos.UserDto;
import com.orion.mdd_api.exceptions.DatabaseException;
import com.orion.mdd_api.exceptions.InvalidDataException;
import com.orion.mdd_api.exceptions.ResourceNotFoundException;
import com.orion.mdd_api.exceptions.UserUnauthorizedException;
import com.orion.mdd_api.mappers.UserMapper;
import com.orion.mdd_api.models.User;
import com.orion.mdd_api.payloads.requests.LoginRequest;
import com.orion.mdd_api.payloads.requests.ProfileUpdateRequest;
import com.orion.mdd_api.payloads.requests.RegisterRequest;
import com.orion.mdd_api.payloads.responses.LoginResponse;
import com.orion.mdd_api.repositories.UserRepository;
import com.orion.mdd_api.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** Service for handling user-related operations. */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final RefreshTokenService refreshTokenService;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final UserMapper userMapper;

  /**
   * Registers a new user to the application
   *
   * @param registerRequest the request to register a new user
   */
  public void registerUser(RegisterRequest registerRequest) {
    User user = new User();
    user.setUsername(registerRequest.getUsername());
    user.setEmail(registerRequest.getEmail());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    saveUser(user);
  }

  /**
   * Saves a user to the database
   *
   * @param user the user to save
   * @return the user saved
   */
  public User saveUser(User user) {
    try {
      return userRepository.save(user);
    } catch (DataIntegrityViolationException ex) {
      String message = "An error occurred.";
      if (userRepository.existsByEmail(user.getEmail())) {
        message = "Email is already taken";
      }
      if (userRepository.existsByUsername(user.getUsername())) {
        message = "Username is already taken";
      }
      throw new InvalidDataException(message, ex);
    } catch (Exception ex) {
      throw new DatabaseException(ex);
    }
  }

  /**
   * Authenticates a user
   *
   * @param loginRequest the user's credentials
   * @return LoginResponse containing the JWT token and the refresh token cookie
   */
  public LoginResponse login(LoginRequest loginRequest) {

    Optional<User> userOptional = userRepository.findByEmail(loginRequest.getLogin());

    if (userOptional.isEmpty()) {
      userOptional = userRepository.findByUsername(loginRequest.getLogin());
    }

    if (userOptional.isEmpty()
        || !passwordEncoder.matches(loginRequest.getPassword(), userOptional.get().getPassword())) {
      throw new UserUnauthorizedException("Invalid credentials");
    }

    User user = userOptional.get();

    Cookie refreshTokenCookie = refreshTokenService.buildRefreshTokenCookie(user);

    return new LoginResponse(jwtUtil.generateAccessToken(user.getEmail()), refreshTokenCookie);
  }

  /**
   * Retrieves the authenticated user details
   *
   * @param userEmail the authenticated user's email address
   * @return the user details
   */
  public UserDto me(String userEmail) {
    return userMapper.toDto(findByEmail(userEmail));
  }

  /**
   * Finds a user by email address
   *
   * @param userEmail the user's email address
   * @return the user found
   */
  public User findByEmail(String userEmail) {
    return userRepository
        .findByEmail(userEmail)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  /**
   * Updates the authenticated user's profile
   *
   * @param profileUpdateRequest the request to update the user's profile
   * @param userEmail the authenticated user's email address
   * @return the updated user
   */
  public UserDto updateProfile(ProfileUpdateRequest profileUpdateRequest, String userEmail) {
    User user = findByEmail(userEmail);
    user.setUsername(profileUpdateRequest.getUsername());
    user.setEmail(profileUpdateRequest.getEmail());
    user = saveUser(user);
    return userMapper.toDto(user);
  }
}
