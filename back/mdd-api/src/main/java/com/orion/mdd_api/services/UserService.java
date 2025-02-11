package com.orion.mdd_api.services;

import com.orion.mdd_api.dtos.UserDto;
import com.orion.mdd_api.exceptions.DatabaseException;
import com.orion.mdd_api.exceptions.InvalidDataException;
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

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final RefreshTokenService refreshTokenService;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final UserMapper userMapper;

  public void registerUser(RegisterRequest registerRequest) {
    User user = new User();
    user.setUsername(registerRequest.getUsername());
    user.setEmail(registerRequest.getEmail());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    saveUser(user);
  }

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

  public UserDto me(String userEmail) {
    return userMapper.toDto(findByEmail(userEmail));
  }

  public User findByEmail(String userEmail) {
    return userRepository
        .findByEmail(userEmail)
        .orElseThrow(() -> new UserUnauthorizedException("User not found"));
  }

  public UserDto updateProfile(ProfileUpdateRequest profileUpdateRequest, String userEmail) {
    User user = findByEmail(userEmail);
    user.setUsername(profileUpdateRequest.getUsername());
    user.setEmail(profileUpdateRequest.getEmail());
    user = saveUser(user);
    return userMapper.toDto(user);
  }
}
