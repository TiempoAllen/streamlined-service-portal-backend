package com.example.streamlined.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.streamlined.backend.Entity.PersonnelScheduleEntity;

public interface PersonnelSchedule extends JpaRepository<PersonnelScheduleEntity, Integer> {

    @Query("SELECT s FROM PersonnelScheduleEntity s WHERE s.personnel.id = :techId")
    List<PersonnelScheduleEntity> findSchedulesByTechnicianId(@Param("techId") Long techId);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END "
            + "FROM PersonnelScheduleEntity s WHERE s.personnel.id = :personnelId "
            + "AND s.status = 'Assigned' "
            + "AND (s.startTime < :endTime AND s.endTime > :startTime)")
    boolean hasConflict(@Param("personnelId") Long personnelId,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime);

    // Optional: Find schedules for personnel within a specific date range (for reports, views, etc.)
    // @Query("SELECT s FROM PersonnelScheduleEntity s WHERE s.personnel.id = :personnelId " +
    //        "AND s.startTime >= :startDate AND s.endTime <= :endDate")
    // List<PersonnelScheduleEntity> findSchedulesWithinDateRange(@Param("personnelId") Long personnelId,
    //                                                            @Param("startDate") LocalDateTime startDate,
    //                                                            @Param("endDate") LocalDateTime endDate);
}
