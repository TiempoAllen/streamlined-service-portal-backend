package com.example.streamlined.backend.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tblRequests")
public class RequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long request_id;

    @Column(name = "description")
    private String description;

    @Column(name = "datetime")
    private String datetime; // Changed to String

    @Column(name = "status")
    private String status;

    @Column(name = "request_technician")
    private String request_technician;

    @Column(name = "request_location")
    private String request_location;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "user_firstname")
    private String user_firstname;

    @Column(name = "user_lastname")
    private String user_lastname;

    @Column(name = "scheduled_start_date")
    private String scheduledStartDate;

    @Column(name = "completed_start_date")
    private String completedStartDate;

    @Lob // Use this for large data
    private String attachment;

    @Column(name = "denial_reason", nullable = true)
    private String denialReason;

    @Column(name = "is_opened", nullable = false)
    private Boolean isOpened = false;

    private String errorMessage;

    @ManyToMany
    @JoinTable(
            name = "request_technicians",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "tech_id")
    )
    // @JsonManagedReference // Prevents infinite recursion
    private List<TechnicianEntity> technicians = new ArrayList<>();

    @Column(name = "technician_id")
    private Long technicianId;

    @Column(name = "is_resubmitted", nullable = true)
    private boolean isResubmitted = false; // Default value

    @Column(name = "remarks")
    private String remarks;

    public RequestEntity() {
        super();
        this.isOpened = false;
    }

    public RequestEntity(Long request_id, String description, String datetime, String status, String request_technician,
            String request_location, Long user_id, String user_firstname, String user_lastname,
            String scheduledStartDate, String completedStartDate, String attachment, String denialReason,
            Boolean isOpened, String errorMessage, List<TechnicianEntity> technicians, Long technicianId,
            boolean isResubmitted, String remarks) {
        this.request_id = request_id;
        this.description = description;
        this.datetime = datetime;
        this.status = status;
        this.request_technician = request_technician;
        this.request_location = request_location;
        this.user_id = user_id;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
        this.scheduledStartDate = scheduledStartDate;
        this.completedStartDate = completedStartDate;
        this.attachment = attachment;
        this.denialReason = denialReason;
        this.isOpened = isOpened;
        this.errorMessage = errorMessage;
        this.technicians = technicians;
        this.technicianId = technicianId;
        this.isResubmitted = isResubmitted;
        this.remarks = remarks;
    }

    public boolean GetIsResubmitted() {
        return isResubmitted;
    }

    public Long getRequest_id() {
        return request_id;
    }

    public void setRequest_id(Long request_id) {
        this.request_id = request_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequest_technician() {
        return request_technician;
    }

    public void setRequest_technician(String request_technician) {
        this.request_technician = request_technician;
    }

    public String getRequest_location() {
        return request_location;
    }

    public void setRequest_location(String request_location) {
        this.request_location = request_location;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUser_firstname() {
        return user_firstname;
    }

    public void setUser_firstname(String user_firstname) {
        this.user_firstname = user_firstname;
    }

    public String getUser_lastname() {
        return user_lastname;
    }

    public void setUser_lastname(String user_lastname) {
        this.user_lastname = user_lastname;
    }

    public String getScheduledStartDate() {
        return scheduledStartDate;
    }

    public void setScheduledStartDate(String scheduledStartDate) {
        this.scheduledStartDate = scheduledStartDate;
    }

    public String getCompletedStartDate() {
        return completedStartDate;
    }

    public void setCompletedStartDate(String completedStartDate) {
        this.completedStartDate = completedStartDate;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getDenialReason() {
        return denialReason;
    }

    public void setDenialReason(String denialReason) {
        this.denialReason = denialReason;
    }

    public Boolean getIsOpened() {
        return isOpened;
    }

    public void setIsOpened(Boolean isOpened) {
        this.isOpened = isOpened;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<TechnicianEntity> getTechnicians() {
        return technicians;
    }

    public void setTechnicians(List<TechnicianEntity> technicians) {
        this.technicians = technicians;
    }

    public Long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }

    public boolean isIsResubmitted() {
        return isResubmitted;
    }

    public void setIsResubmitted(boolean isResubmitted) {
        this.isResubmitted = isResubmitted;

    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
