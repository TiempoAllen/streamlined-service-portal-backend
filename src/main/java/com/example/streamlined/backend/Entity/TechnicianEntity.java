package com.example.streamlined.backend.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tblTechnician")
public class TechnicianEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tech_id;

    private String tech_name;

    private String tech_phone;

    private String tech_gender;

    private String tech_status;

    private String tech_classification;

    private String tech_department;

    @Column(nullable = false)
    private boolean isavailable = true;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // @JoinTable(
    //         name = "request_technicians",
    //         joinColumns = @JoinColumn(name = "tech_id"),
    //         inverseJoinColumns = @JoinColumn(name = "request_id")
    // )
    @JsonBackReference// Prevents infinite recursion
    private List<RequestEntity> requests = new ArrayList<>();

    public TechnicianEntity() {
        super();
    }

    public TechnicianEntity(String tech_classification, String tech_department, String tech_gender, Long tech_id, String tech_name, String tech_phone, String tech_status) {
        this.tech_classification = tech_classification;
        this.tech_department = tech_department;
        this.tech_gender = tech_gender;
        this.tech_id = tech_id;
        this.tech_name = tech_name;
        this.tech_phone = tech_phone;
        this.tech_status = tech_status;
    }

    public Long getTech_id() {
        return tech_id;
    }

    public void setTech_id(Long tech_id) {
        this.tech_id = tech_id;
    }

    public String getTech_name() {
        return tech_name;
    }

    public void setTech_name(String tech_name) {
        this.tech_name = tech_name;
    }

    public String getTech_phone() {
        return tech_phone;
    }

    public void setTech_phone(String tech_phone) {
        this.tech_phone = tech_phone;
    }

    public String getTech_gender() {
        return tech_gender;
    }

    public void setTech_gender(String tech_gender) {
        this.tech_gender = tech_gender;
    }

    public String getTech_status() {
        return tech_status;
    }

    public void setTech_status(String tech_status) {
        this.tech_status = tech_status;
    }

    public String getTech_classification() {
        return tech_classification;
    }

    public void setTech_classification(String tech_classification) {
        this.tech_classification = tech_classification;
    }

    public String getTech_department() {
        return tech_department;
    }

    public void setTech_department(String tech_department) {
        this.tech_department = tech_department;
    }

    public boolean isIsavailable() {
        return isavailable;
    }

    public void setIsavailable(boolean isavailable) {
        this.isavailable = isavailable;
    }

    public List<RequestEntity> getRequests() {
        return requests;
    }

    public void setRequests(List<RequestEntity> requests) {
        this.requests = requests;
    }

}
