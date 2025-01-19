package com.sparta.backendonboardingassignment.domain.tokens.service;

import com.sparta.backendonboardingassignment.domain.tokens.dto.TokenResponseDto;
import com.sparta.backendonboardingassignment.domain.tokens.entity.RefreshToken;
import com.sparta.backendonboardingassignment.domain.tokens.repository.RefreshTokenRepository;
import com.sparta.backendonboardingassignment.domain.users.entity.User;
import com.sparta.backendonboardingassignment.domain.users.repository.UserRepository;
import com.sparta.backendonboardingassignment.global.config.JwtConfig;
import com.sparta.backendonboardingassignment.global.security.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public TokenResponseDto refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtil.getRefreshTokenFromHeader(request);
        Claims userInfo = jwtUtil.getUserInfoFromToken(refreshToken);

        User foundUser = userRepository.findByUsername(userInfo.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );

        RefreshToken foundToken = refreshTokenRepository.findByUserId(foundUser.getId()).orElseThrow(
                () -> new NoSuchElementException("토큰을 찾을 수 없습니다.")
        );

        if (!refreshToken.equals(foundToken.getToken().replace(JwtConfig.BEARER_PREFIX, ""))) {
            throw new NoSuchElementException("재로그인을 해주세요.");
        }

        String newAccessToken = jwtUtil.createAccessToken(foundUser.getUsername(), foundUser.getRole());

        return new TokenResponseDto(newAccessToken.replace(JwtConfig.BEARER_PREFIX, ""));

    }
}
