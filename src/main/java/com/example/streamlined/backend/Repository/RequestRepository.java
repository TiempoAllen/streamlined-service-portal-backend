package com.example.streamlined.backend.Repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.streamlined.backend.Entity.RequestEntity;

public interface RequestRepository extends JpaRepository<RequestEntity, Integer> {
    @Query("SELECT AVG(r.rating) FROM RequestEntity r WHERE r.technicianId = :technicianId AND r.rating IS NOT NULL")
    Double findAverageRatingByTechnicianId(@Param("technicianId") Long technicianId);

    @Query("SELECT r.userFeedback FROM RequestEntity r WHERE r.technicianId = :technicianId AND r.rating >= 4 AND r.userFeedback IS NOT NULL")
    List<String> findPositiveFeedbackByTechnicianId(@Param("technicianId") Long technicianId);

    @Query("SELECT r.userFeedback FROM RequestEntity r WHERE r.technicianId = :technicianId AND r.rating <= 2 AND r.userFeedback IS NOT NULL")
    List<String> findNegativeFeedbackByTechnicianId(@Param("technicianId") Long technicianId);
    
    @Query("SELECT r FROM RequestEntity r WHERE r.user_id = :userId")
    List<RequestEntity> findAllByUserId(@Param("userId") Long userId);

}
