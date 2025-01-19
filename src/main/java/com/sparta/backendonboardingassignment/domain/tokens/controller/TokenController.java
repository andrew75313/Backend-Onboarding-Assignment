package com.sparta.backendonboardingassignment.domain.tokens.controller;

import com.sparta.backendonboardingassignment.domain.tokens.dto.TokenResponseDto;
import com.sparta.backendonboardingassignment.domain.tokens.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/token")
    public ResponseEntity<TokenResponseDto> createToken(HttpServletRequest request) {

        TokenResponseDto responseDto = tokenService.refreshToken(request);

        return ResponseEntity.ok(responseDto);

    }
}
