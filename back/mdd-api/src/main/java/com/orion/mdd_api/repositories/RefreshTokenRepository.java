package com.orion.mdd_api.repositories;

import com.orion.mdd_api.models.RefreshToken;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByTokenAndExpiresAtAfter(String token, Instant now);

  List<RefreshToken> findByUserId(Long userId);

  void deleteByToken(String token);
}
