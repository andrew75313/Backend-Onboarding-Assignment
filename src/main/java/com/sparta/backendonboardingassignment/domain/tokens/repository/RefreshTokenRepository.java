package com.sparta.backendonboardingassignment.domain.tokens.repository;


import com.sparta.backendonboardingassignment.domain.tokens.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long id);
}