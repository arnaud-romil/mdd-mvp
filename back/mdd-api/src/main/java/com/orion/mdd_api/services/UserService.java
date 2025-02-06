package com.orion.mdd_api.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.orion.mdd_api.exceptions.DatabaseException;
import com.orion.mdd_api.exceptions.InvalidDataException;
import com.orion.mdd_api.models.User;
import com.orion.mdd_api.payloads.requests.RegisterRequest;
import com.orion.mdd_api.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

}
