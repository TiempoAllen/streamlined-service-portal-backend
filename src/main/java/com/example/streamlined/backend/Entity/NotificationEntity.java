package com.example.streamlined.backend.Entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tblNotifications")
public class NotificationEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id") 
    private Long notificationId;

    @Column(name = "message")
    private String message;

    @Column(name = "recipient_id")
    private Long recipientId;

    @Column(name = "recipient_role")
    private String recipientRole; // e.g., "Admin", "User"

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "is_read")
    private boolean isRead;

	@Column(name = "notification_type")
	private String notificationType;

	@Column
	private Long request_id;

	public NotificationEntity() {
    }

    // Constructor for basic notification with notification type
    public NotificationEntity(String message, Long recipientId, String recipientRole, String notificationType) {
        this.message = message;
        this.recipientId = recipientId;
        this.recipientRole = recipientRole;
        this.notificationType = notificationType;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.isRead = false;
    }

	public NotificationEntity(String message, Long recipientId, String recipientRole) {
        this.message = message;
        this.recipientId = recipientId;
        this.recipientRole = recipientRole;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.isRead = false;
    }

	public NotificationEntity(String message, Long recipientId, String recipientRole, Long request_id) {
        this.message = message;
        this.recipientId = recipientId;
        this.recipientRole = recipientRole;
		this.request_id = request_id;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.isRead = false;
    }

    public Long getNotification_id() {
        return notificationId;
    }

    public void setNotification_id(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientRole() {
        return recipientRole;
    }

    public void setRecipientRole(String recipientRole) {
        this.recipientRole = recipientRole;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }


    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

	public Long getRequest_id() {
		return request_id;
	}

	public void setRequest_id(Long request_id) {
		this.request_id = request_id;
	}

	
}
