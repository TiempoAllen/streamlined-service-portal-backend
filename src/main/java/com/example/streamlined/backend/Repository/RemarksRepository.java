package com.example.streamlined.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.streamlined.backend.Entity.RemarksEntity;

public interface RemarksRepository extends JpaRepository<RemarksEntity, Long> {

    @Query("SELECT r FROM RemarksEntity r WHERE r.request_id = :requestId")
List<RemarksEntity> findByRequestId(@Param("requestId") Long request_id);


}
