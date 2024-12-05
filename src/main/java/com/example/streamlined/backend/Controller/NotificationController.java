package com.example.streamlined.backend.Controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.streamlined.backend.Entity.NotificationEntity;
import com.example.streamlined.backend.Entity.RequestEntity;
import com.example.streamlined.backend.Repository.RequestRepository;
import com.example.streamlined.backend.Service.NotificationService;
import com.example.streamlined.backend.Service.RequestService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    RequestRepository requestRepository;



     @Autowired
    RequestService requestService; 

    @CrossOrigin(origins = {
        "http://localhost:5173",  // Development environment
        "https://streamlined-service-portal-4amnsogyi-deployed-projects-4069a065.vercel.app","https://streamlined-service-portal.vercel.app/" // Production environment
    }, allowCredentials = "true")
    
    @GetMapping("/{userId}")
    public List<NotificationEntity> getNotifications(@PathVariable Long userId) {
        return notificationService.getNotificationsForUser(userId);
    }
    @PutMapping("/mark-as-read/{notificationId}")
    public ResponseEntity<?> markAsRead(@PathVariable Long notificationId) {
        Optional<NotificationEntity> notificationOpt = notificationService.findById(notificationId);
        if (notificationOpt.isPresent()) {
            NotificationEntity notification = notificationOpt.get();
            notification.setIsRead(true);
            notificationService.save(notification); // Persist change to DB
            return ResponseEntity.ok("Notification marked as read.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found.");
        }
    }
    

    @PutMapping("/notify-evaluation/{request_id}/{userId}")
    public void notifyEvaluation(@PathVariable Long request_id, @PathVariable Long userId) {
    notificationService.notifyUserForEvaluation(request_id, userId);
    }

  

    @PutMapping("/update-status/{request_id}")
    public ResponseEntity<RequestEntity> updateRequestStatus(
            @PathVariable int request_id, 
            @RequestParam String status) {

        RequestEntity request = requestRepository.findById(request_id)
                .orElseThrow(() -> new RuntimeException("Request not found with ID: " + request_id));
        
      
        request.setStatus(status);
    
        if ("COMPLETED".equalsIgnoreCase(status)) {
        
            notificationService.notifyUserForEvaluation((long) request_id, request.getUser_id());
            

        }
    
        requestRepository.save(request);
    
        return ResponseEntity.ok(request);
    }
    


   
}