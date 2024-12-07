package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DAO {
	@Autowired
	UserInterface repo;
	public User findUser(String email) {
		return repo.findByEmail(email);
	}
	public void insert(User user) {
		repo.save(user);
	}
	@Autowired
	WorkshopInterface repo1;
	public List<User> getRegisteredUsers(Long workshopId) {
        return repo1.findUsersByWorkshopId(workshopId);
    }
}
