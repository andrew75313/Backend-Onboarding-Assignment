package com.sparta.backendonboardingassignment.domain.users.dto;

import com.sparta.backendonboardingassignment.domain.users.entity.UsersRoleEnum;
import lombok.Getter;

@Getter
public class SignupResponseDto {

    private String name;
    private String email;
    private UsersRoleEnum role;

}
