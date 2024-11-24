package com.example.streamlined.backend.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.streamlined.backend.Entity.ChatEntity;
import com.example.streamlined.backend.Entity.UserEntity;
import com.example.streamlined.backend.Repository.ChatRepository;
import com.example.streamlined.backend.Repository.UserRepository;

@Service
public class ChatService {

    @Autowired
    private ChatRepository crepo;

    @Autowired
    private UserRepository urepo;

    private final String uploadDir = "uploads/";

    public List<ChatEntity> findByParticipant(String username) {
        Optional<UserEntity> user = urepo.findByUsername(username);
        if (user.isPresent()) {
            return crepo.findByParticipants(user.get());
        } else {
            return new ArrayList<>();  // Return an empty list if user not found
        }
    }


    // Define the saveFile method
    public String saveFile(MultipartFile file, String sender, String receiver) throws Exception {
        // Generate a unique filename
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + filename);

        // Save the file to the specified directory
        Files.createDirectories(filePath.getParent()); // Create directories if not exist
        Files.write(filePath, file.getBytes());

        // Here you could also save information in your database if needed
        // For example, creating a MessageEntity and saving it to the database

        return filename; // Return the saved filename
    }
    
}