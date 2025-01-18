package com.sparta.backendonboardingassignment.domain.users.controller;

import com.sparta.backendonboardingassignment.domain.users.dto.RolenameResponseDto;
import com.sparta.backendonboardingassignment.domain.users.dto.SignupRequestDto;
import com.sparta.backendonboardingassignment.domain.users.dto.SignupResponseDto;
import com.sparta.backendonboardingassignment.domain.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto<RolenameResponseDto>> createUser(@RequestBody @Valid SignupRequestDto requestDto) {

        SignupResponseDto responseDto = userService.signup(requestDto);

        return ResponseEntity.ok(responseDto);

    }



}
