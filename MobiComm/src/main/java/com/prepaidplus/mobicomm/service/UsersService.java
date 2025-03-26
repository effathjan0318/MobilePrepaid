package com.prepaidplus.mobicomm.service;

import com.prepaidplus.mobicomm.model.Users;
import com.prepaidplus.mobicomm.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    // ✅ Fetch all users
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    // ✅ Fetch user by ID
    public Optional<Users> getUserById(Integer id) {
        return usersRepository.findById(id);
    }

    // ✅ Fetch user by Phone Number (for Users)
    public Optional<Users> getUserByPhoneNumber(String phoneNumber) {
        return usersRepository.findByPhoneNumber(phoneNumber);
    }

    // ✅ Fetch user by Username (for Admins)
    public Optional<Users> getUserByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    // ✅ Create a new user
    public Users createUser(Users user) {
        return usersRepository.save(user);
    }

    // ✅ Update user details
    public Users updateUser(Integer id, Users userDetails) {
        return usersRepository.findById(id).map(user -> {
            user.setFullName(userDetails.getFullName());
            user.setPhoneNumber(userDetails.getPhoneNumber());
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            user.setRole(userDetails.getRole());
            user.setAddress(userDetails.getAddress());
            user.setRegisteredAt(userDetails.getRegisteredAt());
            user.setStatus(userDetails.getStatus());
            user.setProfileImageUrl(userDetails.getProfileImageUrl());
            return usersRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ Delete a user
    public void deleteUser(Integer id) {
        usersRepository.deleteById(id);
    }
}
