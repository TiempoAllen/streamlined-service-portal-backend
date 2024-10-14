package com.example.streamlined.backend.Controller;

import java.util.HashMap;
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

import com.example.streamlined.backend.Entity.UserEntity;
import com.example.streamlined.backend.Service.UserService;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
	@Autowired
	UserService userv;

	@GetMapping("/hello")
	public String printHello() {
		return "Hello World!";
	}


	@PostMapping("/add")
    public ResponseEntity<?> insertUser(@RequestBody UserEntity user) {
        try {
            UserEntity newUser = userv.insertUser(user);
            return ResponseEntity.ok(newUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/updateUser")
	public UserEntity updateUser(@RequestParam int uid, @RequestBody UserEntity newUserDetails) {
		return userv.updateUser(uid, newUserDetails);
	}
	

	@GetMapping("/all")
	public List<UserEntity> getAllUsers(){
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
	public String deleteUser (@PathVariable int user_id) {
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
}
