package com.example.streamlined.backend.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.streamlined.backend.Entity.NotificationEntity;
import com.example.streamlined.backend.Entity.RequestEntity;
import com.example.streamlined.backend.Repository.NotificationRepository;
import com.example.streamlined.backend.Repository.RequestRepository;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    RequestRepository requestRepository;

    public NotificationEntity addNotification(String message, Long recipientId, String recipientRole) {
        NotificationEntity notification = new NotificationEntity(message, recipientId, recipientRole);
        return notificationRepository.save(notification);
    }

    public void addNotificationStatus(String message, Long recipientId, String recipientRole, String notificationType, Long request_id) {
        NotificationEntity notification = new NotificationEntity();
        notification.setMessage(message);
        notification.setRecipientId(recipientId);
        notification.setRecipientRole(recipientRole);
        notification.setTimestamp(new Timestamp(System.currentTimeMillis()));
        notification.setRead(false);
        notification.setNotificationType(notificationType);
    
        // Logic to fetch request_id based on certain conditions
        if (request_id != null) {
            RequestEntity request = requestRepository.findById(request_id.intValue())
                .orElseThrow(() -> new IllegalArgumentException("Request not found for ID: " + request_id));
    
            // You can fetch the request_id without the @JoinColumn relationship
            Long requestId = request.getRequest_id(); // Or any other logic for request_id
            notification.setRequest_id(requestId); // Storing only the request_id in notification
        }
    
        notificationRepository.save(notification);
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

    public void notifyUserForEvaluation(Long requestId, Long userId) {
        String message = "Your request (ID: " + requestId + ") has been completed. Please provide feedback.";
        addNotificationStatus(message, userId, "USER", "EVALUATION", requestId);
    }
    
    
}
