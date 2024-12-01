package com.example.streamlined.backend.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.streamlined.backend.Entity.UserEntity;
import com.example.streamlined.backend.Repository.UserRepository;
import com.example.streamlined.backend.Service.UserService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {
    "http://localhost:5173", "http://localhost:3000", // Development environment
    "https://streamlined-service-portal-4amnsogyi-deployed-projects-4069a065.vercel.app", "https://streamlined-service-portal.vercel.app/" // Production environment
}, allowCredentials = "true")
public class UserController {

    @Autowired
    UserService userv;

    @Autowired
    UserRepository urepo;

    @GetMapping("/hello")
    public String printHello() {
        return "Hello World!";
    }

    @PostMapping("/add")
    public ResponseEntity<?> insertUser(@RequestBody UserEntity user) {
        try {
            UserEntity newUser = userv.insertUser(user);
            // Create a copy of user without password
            UserEntity safeUser = new UserEntity();
            safeUser.setUser_id(newUser.getUser_id());
            safeUser.setUsername(newUser.getUsername());
            safeUser.setEmail(newUser.getEmail());
            // Copy other non-sensitive fields

            return ResponseEntity.ok(safeUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/updateUser")
    public UserEntity updateUser(@RequestParam int uid, @RequestBody UserEntity newUserDetails) {
        return userv.updateUser(uid, newUserDetails);
    }

    @GetMapping("/all")
    public List<UserEntity> getAllUsers() {
        return userv.getAllUsers();
    }

    @GetMapping("/{user_id}")
    public Optional<UserEntity> getUserById(@PathVariable int user_id) {
        Optional<UserEntity> user = userv.getUserById(user_id);
        return user;
    }

    /*@PutMapping("/updateUser")
	public UserEntity updateUser(@RequestParam int uid, @RequestBody UserEntity newUserDetails) {
		return userv.updateUser(uid, newUserDetails);
	}*/
    @DeleteMapping("/{user_id}")
    public String deleteUser(@PathVariable int user_id) {
        return userv.deleteUser(user_id);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserEntity loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Map<String, Object> loginResponse = userv.loginUser(email, password);

        if (loginResponse != null) {
            // Successful login
            return ResponseEntity.ok(loginResponse);
        } else {
            // Invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/uploadProfilePicture/{user_id}")
    public String uploadProfilePicture(Long user_id, MultipartFile file) {
        UserEntity user = urepo.findByUserId(user_id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Define the uploads directory
        String uploadDirectory = "uploads/";
        Path uploadPath = Paths.get(uploadDirectory);

        try {
            // Ensure the uploads directory exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the file in the uploads directory
            String fileName = "profile_" + user_id + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            // Save the file path to the database
            user.setProfile_picture(filePath.toString());
            urepo.save(user);

            return "Profile picture uploaded successfully to " + filePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture: " + e.getMessage());
        }
    }

    @GetMapping("/{user_id}/profile-picture")
    public byte[] getProfilePicture(Long user_id) {
        UserEntity user = urepo.findByUserId(user_id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + user_id + " not found"));

        // Retrieve the file path from the user entity
        String filePath = user.getProfile_picture();
        if (filePath == null || filePath.isEmpty()) {
            throw new RuntimeException("Profile picture not found for user ID " + user_id);
        }

        try {
            // Read the file as a byte array
            Path path = Paths.get(filePath);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read profile picture: " + e.getMessage());
        }
    }

}
