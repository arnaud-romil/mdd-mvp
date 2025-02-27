package com.orion.mdd_api.repositories;

import com.orion.mdd_api.models.RefreshToken;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository for managing RefreshToken entities */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByTokenAndExpiresAtAfter(String token, Instant now);

  /**
   * Finds a List of refresh tokens for a user
   *
   * @param userId the id of the user
   * @return List containing the refresh tokens found
   */
  List<RefreshToken> findByUserId(Long userId);

  /**
   * Deletes a refreshToken
   *
   * @param token the token to delete
   */
  void deleteByToken(String token);
}
