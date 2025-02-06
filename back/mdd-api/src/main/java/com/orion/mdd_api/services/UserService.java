package com.orion.mdd_api.services;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.orion.mdd_api.exceptions.DatabaseException;
import com.orion.mdd_api.exceptions.InvalidDataException;
import com.orion.mdd_api.exceptions.UserUnauthorizedException;
import com.orion.mdd_api.models.User;
import com.orion.mdd_api.payloads.requests.LoginRequest;
import com.orion.mdd_api.payloads.requests.RegisterRequest;
import com.orion.mdd_api.payloads.responses.LoginResponse;
import com.orion.mdd_api.repositories.UserRepository;
import com.orion.mdd_api.utils.JwtUtil;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User registerUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return saveUser(user);
    }

    private User saveUser(User user) {
        try {
            return userRepository.save(user);
        }
        catch (DataIntegrityViolationException ex) {
            String message = "An error occurred.";
            if(userRepository.existsByEmail(user.getEmail())) {
                message = "Email is already taken";
            }
            if(userRepository.existsByUsername(user.getUsername())) {
                message = "Username is already taken";
            }
            throw new InvalidDataException(message, ex);
        }        
        catch (Exception ex) {
          throw new DatabaseException(ex);
        }
    }

    public LoginResponse login(LoginRequest loginRequest) {
        
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getLogin());
 
        if(userOptional.isEmpty()) {
            userOptional = userRepository.findByUsername(loginRequest.getLogin());
        }
 
        if(userOptional.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), userOptional.get().getPassword())) {
            throw new UserUnauthorizedException("Invalid credentials");
        }

        return new LoginResponse(jwtUtil.generateAccessToken(userOptional.get().getEmail()));   
    }

}
