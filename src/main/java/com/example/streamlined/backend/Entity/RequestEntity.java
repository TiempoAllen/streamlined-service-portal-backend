package com.example.streamlined.backend.Entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tblRequests")
public class RequestEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long request_id;

	private String title;

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

    @Column(name = "department")
    private String department;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "user_firstname")
    private String user_firstname;

    @Column(name = "user_lastname")
    private String user_lastname;

    @Column(name = "start_time")
    private String startTime; // Changed to String

    @Column(name = "end_time")
    private String endTime; // Changed to String

    @Column(name = "attachment")
    private String attachment;

    @Column(name = "denial_reason", nullable = true)
    private String denialReason;

    private String errorMessage;

    @ManyToOne
    @JoinColumn(name = "tech_id", nullable = true)
    @JsonBackReference  // Prevents infinite recursion
    private TechnicianEntity technician;

    @Column(name = "technician_id")
    private Long technicianId;

	public RequestEntity() {
		super();
	}

	public RequestEntity(Long request_id, String title, String description, String datetime, String status,
			String request_technician, String request_location, String department, Long user_id, String user_firstname,
			String user_lastname, String startTime, String endTime, String attachment, String denialReason,
			String errorMessage, TechnicianEntity technician, Long technicianId) {
		super();
		this.request_id = request_id;
		this.title = title;
		this.description = description;
		this.datetime = datetime;
		this.status = status;
		this.request_technician = request_technician;
		this.request_location = request_location;
		this.department = department;
		this.user_id = user_id;
		this.user_firstname = user_firstname;
		this.user_lastname = user_lastname;
		this.startTime = startTime;
		this.endTime = endTime;
		this.attachment = attachment;
		this.denialReason = denialReason;
		this.errorMessage = errorMessage;
		this.technician = technician;
		this.technicianId = technicianId;
	}

	public Long getRequest_id() {
		return request_id;
	}

	public void setRequest_id(Long request_id) {
		this.request_id = request_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
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

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public TechnicianEntity getTechnician() {
		return technician;
	}

	public void setTechnician(TechnicianEntity technician) {
		this.technician = technician;
	}

	public Long getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(Long technicianId) {
		this.technicianId = technicianId;
	}


}
