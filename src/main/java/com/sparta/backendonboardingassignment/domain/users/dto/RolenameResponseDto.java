package com.sparta.backendonboardingassignment.domain.users.dto;

import com.sparta.backendonboardingassignment.domain.users.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class RolenameResponseDto {

    private String authorityName;

    public RolenameResponseDto(UserRoleEnum role) {
        this.authorityName = role.getAuthority();
    }

}
