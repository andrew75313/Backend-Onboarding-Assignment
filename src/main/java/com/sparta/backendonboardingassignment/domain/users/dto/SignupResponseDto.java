package com.sparta.backendonboardingassignment.domain.users.dto;

import com.sparta.backendonboardingassignment.domain.users.entity.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupResponseDto<T> {

    private String username;
    private String nickname;
    private T authorities;

    public SignupResponseDto(String username,String nickname, T authorities) {
        this.username = username;
        this.nickname = nickname;
        this.authorities = authorities;
    }

}
