package com.example.streamlined.backend.Controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

<<<<<<< HEAD
    @Autowired
    RequestRepository requestRepository;

    @CrossOrigin(origins = "http://localhost:3000")
=======
    @CrossOrigin(origins = {
        "http://localhost:5173",  // Development environment
        "https://cituserviceportal-gdrksvm3q-deployed-projects-4069a065.vercel.app" // Production environment
    }, allowCredentials = "true")
    
>>>>>>> master
    @GetMapping("/{userId}")
    public List<NotificationEntity> getNotifications(@PathVariable Long userId) {
        return notificationService.getNotificationsForUser(userId);
    }

    @PutMapping("/mark-as-read/{notificationId}")
    public void markAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
    }

    @PutMapping("/notify-evaluation/{request_id}/{userId}")
    public void notifyEvaluation(@PathVariable Long request_id, @PathVariable Long userId) {
    notificationService.notifyUserForEvaluation(request_id, userId);
    }

     @PutMapping("/{request_id}")
    public RequestEntity submitEvaluation(
            int request_id,
            @RequestParam Integer rating,
            @RequestParam String feedback) {
        RequestEntity request = requestRepository.findById(request_id).get();
        request.setRating(rating);
        request.setUserFeedback(feedback);
        return requestRepository.save(request);
    }

}