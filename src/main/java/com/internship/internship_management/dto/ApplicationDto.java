package com.internship.internship_management.dto;

import java.time.LocalDateTime;

public class ApplicationDto {

    private Long id;
    private Long internId;      // Only need ID, not full object
    private Long companyId;     // Only need ID, not full object
    private String status;      // PENDING, ACCEPTED, REJECTED
    private LocalDateTime applicationDate;

    // Constructors
    public ApplicationDto() {}

    public ApplicationDto(Long id, Long internId, Long companyId,
                          String status, LocalDateTime applicationDate) {
        this.id = id;
        this.internId = internId;
        this.companyId = companyId;
        this.status = status;
        this.applicationDate = applicationDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInternId() {
        return internId;
    }

    public void setInternId(Long internId) {
        this.internId = internId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }
}