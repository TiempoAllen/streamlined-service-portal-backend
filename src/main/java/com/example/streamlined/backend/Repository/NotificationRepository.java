package com.example.streamlined.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.streamlined.backend.Entity.NotificationEntity;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
	List<NotificationEntity> findByRecipientId(Long recipientId);
}
