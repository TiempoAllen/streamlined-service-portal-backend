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

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{userId}")
    public List<NotificationEntity> getNotifications(@PathVariable Long userId) {
        return notificationService.getNotificationsForUser(userId);
    }

    @PutMapping("/mark-as-read/{notificationId}")
    public void markAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
    }
}