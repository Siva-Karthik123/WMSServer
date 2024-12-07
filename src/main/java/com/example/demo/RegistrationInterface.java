package com.example.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationInterface extends JpaRepository<Registration, Long> {
	Optional<Registration> findById(Long userId);
	boolean existsByUserIdAndWorkshopId(Long userId, Long workshopId);
}
