package com.sparta.backendonboardingassignment.domain.tokens.dto;

import lombok.Getter;

@Getter
public class TokenResponseDto {

    String token;

    public TokenResponseDto(String token) {
        this.token = token;
    }

}
