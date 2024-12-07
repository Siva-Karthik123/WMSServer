package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkshopInterface extends JpaRepository<Workshop, Long> {
	@Query("SELECT w.registeredUsers FROM Workshop w WHERE w.id = :workshopId")
    List<User> findUsersByWorkshopId(@Param("workshopId") Long workshopId);
	
	Optional<Workshop> findById(Long id);
}
