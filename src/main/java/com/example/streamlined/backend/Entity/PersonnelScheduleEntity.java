package com.example.streamlined.backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tblPersonnelSchedule")
public class PersonnelScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long schedule_id;

    @ManyToOne
    @JoinColumn(name = "tech_id", nullable = false)
    private TechnicianEntity personnel;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private RequestEntity request;

    @Column(name = "start_time", nullable = false)
    private String startTime;

    @Column(name = "end_time", nullable = false)
    private String endTime;

    @Column(name = "status", nullable = false)
    private String status;

    public PersonnelScheduleEntity() {
    }

    public PersonnelScheduleEntity(String endTime, TechnicianEntity personnel, RequestEntity request, Long schedule_id, String startTime, String status) {
        this.endTime = endTime;
        this.personnel = personnel;
        this.request = request;
        this.schedule_id = schedule_id;
        this.startTime = startTime;
        this.status = status;
    }

    public Long getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(Long schedule_id) {
        this.schedule_id = schedule_id;
    }

    public TechnicianEntity getPersonnel() {
        return personnel;
    }

    public void setPersonnel(TechnicianEntity personnel) {
        this.personnel = personnel;
    }

    public RequestEntity getRequest() {
        return request;
    }

    public void setRequest(RequestEntity request) {
        this.request = request;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // public boolean conflictsWith(String requestedStartTime, String requestedEndTime) {
    //     return (startTime.isBefore(requestedEndTime) && endTime.isAfter(requestedStartTime));
    // }
}
