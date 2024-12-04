package com.example.streamlined.backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.streamlined.backend.Entity.RequestEntity;

public interface RequestRepository extends JpaRepository<RequestEntity, Integer> {
    
    @Query("SELECT r FROM RequestEntity r WHERE r.user_id = :userId")
    List<RequestEntity> findAllByUserId(@Param("userId") Long userId);

}
