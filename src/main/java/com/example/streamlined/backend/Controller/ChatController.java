package com.example.streamlined.backend.Controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.streamlined.backend.Entity.ChatEntity;
import com.example.streamlined.backend.Entity.MessageEntity;
import com.example.streamlined.backend.Entity.UserEntity;
import com.example.streamlined.backend.Repository.ChatRepository;
import com.example.streamlined.backend.Repository.MessageRepository;
import com.example.streamlined.backend.Repository.UserRepository;
import com.example.streamlined.backend.Service.ChatService;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {
    @Autowired
    private SimpMessagingTemplate smtemp;

    @Autowired
    private ChatRepository crepo;

    @Autowired
    private MessageRepository mrepo;

    @Autowired
    private UserRepository urepo;

    @Autowired
    private ChatService cserv;


    @MessageMapping("/chat/{to}")
    public void sendMessage(@DestinationVariable String to, MessageEntity message){
        System.out.println("send message: " + message + "to: " + to);
        message.setChatId(createAndOrGetChat(to));
        message.setTimestamp(generateTimeStamp());
        message = mrepo.save(message);
        smtemp.convertAndSend("/topic/messages/" + to, message);
    }

    @PostMapping("/getChats")
    public List<ChatEntity> getChats(@RequestBody String username){ 
        // Fetch the user by username from the UserRepository
        Optional<UserEntity> userEntityOptional = urepo.findByUsername(username);
    
        if (userEntityOptional.isPresent()) {
            // If user exists, call findByParticipant
            UserEntity userEntity = userEntityOptional.get();
            return crepo.findByParticipants(userEntity);
        } else {
            // Return an empty list if the user is not found
            return new ArrayList<>();
        }
    }

    @PostMapping("/getMessages")
    public List<MessageEntity> getMessages(@RequestBody String chat){
        ChatEntity ce = crepo.findByName(chat);
        if(ce != null){
            return mrepo.findByChatId(ce.getChatId());
        }else{
            return new ArrayList<MessageEntity>();
        }
    }

    

    @PostMapping("/uploadFile")
public ResponseEntity<String> uploadFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("sender") String sender,
        @RequestParam("receiver") String receiver) {
    try {
        String filename = cserv.saveFile(file, sender, receiver);
        
        // Construct the response with the file URL
        String fileUrl = "/chat/download/" + filename; // Adjust according to your URL structure
        return ResponseEntity.ok(fileUrl); // Return just the URL as a string
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
    }
}


    private Long createAndOrGetChat(String name){
        ChatEntity ce = crepo.findByName(name);

        if(ce != null){
            return ce.getChatId();
        }else{
            ChatEntity newChat = new ChatEntity(name);
            return crepo.save(newChat).getChatId();
        }
    }

    private String generateTimeStamp() {
    // Get current instant
    Instant now = Instant.now();
    
    // Convert Instant to LocalDateTime in the system's default timezone
    LocalDateTime localDateTime = LocalDateTime.ofInstant(now, ZoneId.systemDefault());

    // Format the date and time
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    // Generate formatted date and time strings
    String date = localDateTime.format(dateFormatter);
    String time = localDateTime.format(timeFormatter);

    // Combine date and time into the desired format
    String timeStamp = date + " - " + time;
    return timeStamp;
    }

    

}
