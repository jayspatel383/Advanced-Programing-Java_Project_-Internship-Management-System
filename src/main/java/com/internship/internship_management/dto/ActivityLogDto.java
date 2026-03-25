package com.internship.internship_management.dto;

import java.time.LocalDateTime;

public class ActivityLogDto {

    private Long id;
    private Long internId;
    private String action;
    private LocalDateTime timestamp;

    // 1. Default constructor (no arguments) - REQUIRED for JPA/Jackson
    public ActivityLogDto() {}

    // 2. Constructor for creating new logs (without ID)
    public ActivityLogDto(Long internId, String action, LocalDateTime timestamp) {
        this.internId = internId;
        this.action = action;
        this.timestamp = timestamp;
    }

    // 3. Constructor for returning logs with ID (for responses)
    public ActivityLogDto(Long id, Long internId, String action, LocalDateTime timestamp) {
        this.id = id;
        this.internId = internId;
        this.action = action;
        this.timestamp = timestamp;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}