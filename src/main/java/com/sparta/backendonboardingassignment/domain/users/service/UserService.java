package com.sparta.backendonboardingassignment.domain.users.service;

import com.sparta.backendonboardingassignment.domain.users.dto.SignupRequestDto;
import com.sparta.backendonboardingassignment.domain.users.entity.User;
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

    public void signup(SignupRequestDto requestDto) {

        if (isUserExist(requestDto.getUsername())) {
            throw new RuntimeException("동일한 회원이 존재 합니다.");
        }

        String password = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(requestDto);

        userRepository.save(user);

    }

    public Boolean isUserExist(String username) {

        final Optional<User> user = userRepository.findByUsername(username);

        return user.isPresent();

    }
}
