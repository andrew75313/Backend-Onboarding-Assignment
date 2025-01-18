package com.sparta.backendonboardingassignment.domain.users.dto;

import com.sparta.backendonboardingassignment.domain.users.entity.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupResponseDto {

    private String name;
    private String email;
    private UserRoleEnum role;

}
