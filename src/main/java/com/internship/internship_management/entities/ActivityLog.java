package com.internship.internship_management.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "intern_id")
    private Intern intern;

    public ActivityLog() {
    }

    public ActivityLog(String action, LocalDateTime timestamp, Intern intern) {
        this.action = action;
        this.timestamp = timestamp;
        this.intern = intern;
    }

    public Long getId() { return id; }

    public String getAction() { return action; }

    public void setAction(String action) { this.action = action; }

    public LocalDateTime getTimestamp() { return timestamp; }

    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Intern getIntern() { return intern; }

    public void setIntern(Intern intern) { this.intern = intern; }
}
