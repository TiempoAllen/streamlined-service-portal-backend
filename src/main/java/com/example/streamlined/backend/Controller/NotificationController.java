package com.example.streamlined.backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.streamlined.backend.Entity.NotificationEntity;
import com.example.streamlined.backend.Service.NotificationService;
import com.example.streamlined.backend.Service.RequestService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

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
    public void markAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
    }

   
}