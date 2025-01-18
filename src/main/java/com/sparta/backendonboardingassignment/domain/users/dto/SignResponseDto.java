package com.sparta.backendonboardingassignment.domain.users.dto;

import lombok.Getter;

@Getter
public class SignResponseDto {

    String token;

    public SignResponseDto(String token) {
        this.token = token;
    }

}
