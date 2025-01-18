package com.sparta.backendonboardingassignment.domain.users.dto;

import com.sparta.backendonboardingassignment.domain.users.entity.UserRoleEnum;

public class RolenameResponseDto {

    private UserRoleEnum authorityName;

    public RolenameResponseDto(UserRoleEnum role) {
        this.authorityName = role;
    }

}
