package com.example.streamlined.backend.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.streamlined.backend.Entity.NotificationEntity;
import com.example.streamlined.backend.Repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public NotificationEntity addNotification(String message, Long recipientId, String recipientRole) {
        NotificationEntity notification = new NotificationEntity(message, recipientId, recipientRole);
        return notificationRepository.save(notification);
    }

    public List<NotificationEntity> getNotificationsForUser(Long userId) {
        return notificationRepository.findByRecipientId(userId);
    }

    public void markNotificationAsRead(Long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NoSuchElementException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void notifyUserForEvaluation(Long request_id, Long userId) {
        String message = "Your request (ID: " + request_id + ") has been completed. Please provide feedback.";
        addNotification(message, userId, "USER");
    }
    
}
