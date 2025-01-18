package com.sparta.backendonboardingassignment.domain.users.repository;

import com.sparta.backendonboardingassignment.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long>{
}
