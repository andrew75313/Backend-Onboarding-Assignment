package com.sparta.backendonboardingassignment.global.security;

import com.sparta.backendonboardingassignment.domain.tokens.entity.RefreshToken;
import com.sparta.backendonboardingassignment.domain.tokens.repository.RefreshTokenRepository;
import com.sparta.backendonboardingassignment.domain.users.dto.SignRequestDto;
import com.sparta.backendonboardingassignment.domain.users.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.backendonboardingassignment.global.config.JwtConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        setFilterProcessesUrl("/api/sign");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            SignRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), SignRequestDto.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {

        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();

        String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername());
        response.addHeader(JwtConfig.REFRESH_TOKEN_HEADER, refreshToken);

        RefreshToken existingToken = refreshTokenRepository.findByUserId(user.getId()).orElse(null);

        if (existingToken != null) {
            refreshTokenRepository.delete(existingToken);
        }

        refreshTokenRepository.save(new RefreshToken(refreshToken, user));

        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + accessToken.replace("Bear ", "") + "\"}");
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write("로그인에 실패했습니다.");
        response.getWriter().flush();
    }

}
