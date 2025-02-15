package com.orion.mdd_api.services;

import com.orion.mdd_api.exceptions.UserUnauthorizedException;
import com.orion.mdd_api.models.RefreshToken;
import com.orion.mdd_api.models.User;
import com.orion.mdd_api.payloads.responses.LoginResponse;
import com.orion.mdd_api.repositories.RefreshTokenRepository;
import com.orion.mdd_api.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  @Value("${jwt.refreshtoken.validity}")
  private int refreshTokenValidity;

  @Value("${jwt.refreshtoken.secure-cookie}")
  private boolean secureCookie;

  private static final String COOKIE_NAME = "refreshToken";
  private static final String COOKIE_PATH = "/";

  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtUtil jwtUtil;

  private void saveRefreshToken(RefreshToken refreshToken) {
    refreshTokenRepository.save(refreshToken);
  }

  private RefreshToken generateRefreshToken(User user) {

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setUser(user);
    refreshToken.setCreatedAt(Instant.now());
    refreshToken.setExpiresAt(Instant.now().plusSeconds(refreshTokenValidity));

    return refreshToken;
  }

  public LoginResponse refreshAccessToken(String token) {

    RefreshToken refreshToken =
        refreshTokenRepository
            .findByTokenAndExpiresAtAfter(token, Instant.now())
            .orElseThrow(
                () -> new UserUnauthorizedException("Could not find valid refresh token."));

    return new LoginResponse(jwtUtil.generateAccessToken(refreshToken.getUser().getEmail()));
  }

  @Transactional
  public Cookie revokeRefreshToken(User user) {

    List<RefreshToken> refreshTokenList = refreshTokenRepository.findByUserId(user.getId());

    for (RefreshToken refreshToken : refreshTokenList) {
      refreshTokenRepository.deleteById(refreshToken.getId());
    }

    Cookie refreshTokenCookie = new Cookie(COOKIE_NAME, "");
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(secureCookie);
    refreshTokenCookie.setPath(COOKIE_PATH);
    refreshTokenCookie.setMaxAge(0);
    if (secureCookie) {
      refreshTokenCookie.setAttribute("SameSite", "None");
    }

    return refreshTokenCookie;
  }

  public Cookie buildRefreshTokenCookie(User user) {

    RefreshToken refreshToken = generateRefreshToken(user);
    saveRefreshToken(refreshToken);

    Cookie refreshTokenCookie = new Cookie(COOKIE_NAME, refreshToken.getToken());
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(secureCookie);
    refreshTokenCookie.setPath(COOKIE_PATH);
    refreshTokenCookie.setMaxAge(refreshTokenValidity);
    if (secureCookie) {
      refreshTokenCookie.setAttribute("SameSite", "None");
    }

    return refreshTokenCookie;
  }
}
