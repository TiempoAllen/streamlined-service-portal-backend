package com.example.streamlined.backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tblRemarks")
public class RemarksEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long remark_id;

    @Column(name = "request_id")
    private Long request_id;

    @Column
    private String content;

    @Column
    private String createdBy;

    @Column
    private String datetime;

    @Column
    private String status;

    public RemarksEntity(Long remark_id, Long request_id, String content, String createdBy, String datetime, String status) {
        this.remark_id = remark_id;
        this.request_id = request_id;
        this.content = content;
        this.createdBy = createdBy;
        this.datetime = datetime;
        this.status = status;
    }

    public RemarksEntity() {
        super();
    }

    public Long getRemark_id() {
        return remark_id;
    }

    public void setRemark_id(Long remark_id) {
        this.remark_id = remark_id;
    }

    public Long getRequest_id() {
        return request_id;
    }

    public void setRequest_id(Long request_id) {
        this.request_id = request_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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


    
    

}
