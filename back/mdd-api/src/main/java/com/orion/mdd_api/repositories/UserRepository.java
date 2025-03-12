package com.orion.mdd_api.repositories;

import com.orion.mdd_api.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository for managing User entities */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Finds a user by username
   *
   * @param username the username of the user
   * @return an optional user
   */
  Optional<User> findByUsername(String username);

  /**
   * Finds a user by email.
   *
   * @param email the email address
   * @return an optional user
   */
  Optional<User> findByEmail(String email);

  /**
   * Checks if a user with the given email address exists in the database.
   *
   * @param email the email address to check for existence
   * @return {@code true} if a user with the given email address exists, {@code false} otherwise
   */
  boolean existsByEmail(String email);

  /**
   * Checks if a user with the given username exists in the database.
   *
   * @param username the username to check for existence
   * @return {@code true} if a user with the given username exists, {@code false} otherwise
   */
  boolean existsByUsername(String username);
}
