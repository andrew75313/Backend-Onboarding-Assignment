package com.sparta.backendonboardingassignment.domain.users.service;

import com.sparta.backendonboardingassignment.domain.users.dto.*;
import com.sparta.backendonboardingassignment.domain.users.entity.User;
import com.sparta.backendonboardingassignment.domain.users.entity.UserRoleEnum;
import com.sparta.backendonboardingassignment.global.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.sparta.backendonboardingassignment.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public SignupResponseDto signup(SignupRequestDto requestDto) {

        if (isUserExist(requestDto.getUsername())) {
            throw new IllegalArgumentException("동일한 회원이 존재 합니다.");
        }

        String password = passwordEncoder.encode(requestDto.getPassword());

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(password)
                .nickname(requestDto.getNickname())
                .role(UserRoleEnum.USER)
                .build();

        userRepository.save(user);

        return new SignupResponseDto<RolenameResponseDto>(requestDto.getUsername(),
                requestDto.getNickname(),
                new RolenameResponseDto(UserRoleEnum.USER));

    }

    public Boolean isUserExist(String username) {

        final Optional<User> user = userRepository.findByUsername(username);

        return user.isPresent();

    }
}
