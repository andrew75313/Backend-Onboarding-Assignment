package com.sparta.backendonboardingassignment.domain.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignRequestDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

}
