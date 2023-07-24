package com.example.urlsorting.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.urlsorting.user.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUserId(Long userId);

	Optional<User> findByEmail(String email);

	User findByToken(String token);
}