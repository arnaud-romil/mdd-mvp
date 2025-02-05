package com.orion.mdd_api.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.orion.mdd_api.exceptions.DatabaseException;
import com.orion.mdd_api.exceptions.InvalidDataException;
import com.orion.mdd_api.models.User;
import com.orion.mdd_api.payloads.requests.RegisterRequest;
import com.orion.mdd_api.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        return saveUser(user);
    }

    private User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidDataException("Email or Username is already taken", ex);
        } catch (Exception ex) {
          throw new DatabaseException(ex);
        }
    }

}
