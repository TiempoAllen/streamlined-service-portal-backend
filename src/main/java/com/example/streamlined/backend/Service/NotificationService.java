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
}
