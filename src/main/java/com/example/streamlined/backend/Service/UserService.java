package com.example.streamlined.backend.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.streamlined.backend.Config.JwtUtil;
import com.example.streamlined.backend.Entity.UserEntity;
import com.example.streamlined.backend.Repository.UserRepository;


@Service
public class UserService {
	@Autowired
	UserRepository urepo;

	@Autowired
    JwtUtil jwtUtil;

	public Map<String, Object> loginUser(String email, String password) {
        // Find user by email
        UserEntity user = urepo.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            // Password matches
            // Generate JWT token
            String token = jwtUtil.generateToken(email);

            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user_id", user.getUser_id());
            response.put("user", user);

            return response;
        } else {
            // Invalid credentials
            return null;
        }
    }

	public UserEntity insertUser(UserEntity user) {
		String email = user.getEmail();
	    if (urepo.existsByEmail(email)) {
	        throw new IllegalArgumentException("Email already exists");
	    }
	    return urepo.save(user);
	}

	public List<UserEntity> getAllUsers() {
		return urepo.findAll();
	}

	public Optional<UserEntity> getUserById(int user_id) {
		return urepo.findById(user_id);
	}

	@SuppressWarnings("finally")
public UserEntity updateUser(int user_id, UserEntity newUserDetails) {
    try {
        // Find the existing user by ID
        UserEntity existingUser = urepo.findById(user_id).orElseThrow(
            () -> new NoSuchElementException("User " + user_id + " does not exist!")
        );

        // Set all new user details
        existingUser.setUsername(newUserDetails.getUsername());
        existingUser.setFirstname(newUserDetails.getFirstname());
        existingUser.setLastname(newUserDetails.getLastname());
        existingUser.setEmail(newUserDetails.getEmail());
        existingUser.setPassword(newUserDetails.getPassword());
        existingUser.setEmployee_id(newUserDetails.getEmployee_id());
        existingUser.setDepartment(newUserDetails.getDepartment());
//existingUser.setIsadmin(newUserDetails.getIsadmin());
  //      existingUser.setIsSuperUser(newUserDetails.getIsSuperUser());

        // Save and return the updated user
        return urepo.save(existingUser);

    } catch (NoSuchElementException ex) {
        throw new NoSuchElementException("User " + user_id + " does not exist!");
    }
}

public String deleteUser(int userId) {
	// Assuming you have a UserRepository or similar
	if (urepo.existsById(userId)) {
		urepo.deleteById(userId);
		return "User deleted successfully";
	} else {
		return "User not found";
	}
}
}
