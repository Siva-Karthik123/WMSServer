package com.example.demo;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*;
import java.util.*;

import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;

@Entity
public class Workshop {
	@ManyToMany
    @JoinTable(
        name = "workshop_registrations",
        joinColumns = @JoinColumn(name = "workshop_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
	private Set<User> registeredUsers = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String instructor;
    private String Company;
    private String description; // Store as a comma-separated string
    private String benefits; // Store as a comma-separated string
    private String date;
    private String time;
    private int availableSlots;
    private String image;
    private int attendance;

    // Getters and Setters


    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBenefits() {
		return benefits;
	}

	public void setBenefits(String benefits) {
		this.benefits = benefits;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getAvailableSlots() {
		return availableSlots;
	}

	public void setAvailableSlots(int availableSlots) {
		this.availableSlots = availableSlots;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getAttendance() {
		return attendance;
	}

	public void setAttendance(int attendance) {
		this.attendance = attendance;
	}

	public Set<User> getRegisteredUsers() {
		return registeredUsers;
	}

	public void setRegisteredUsers(Set<User> registeredUsers) {
		this.registeredUsers = registeredUsers;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	public String getCompany() {
		return Company;
	}

	public void setCompany(String company) {
		Company = company;
	}

	
	
}
