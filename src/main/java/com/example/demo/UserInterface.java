package com.example.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInterface extends JpaRepository<User , Long> {
	public User findByEmail(String email);
	Optional<User> findById(Long userId);
}
