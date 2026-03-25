package com.internship.internship_management.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "internship_applications")
public class InternshipApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "intern_id", nullable = false)
    private Intern intern;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private String status; // PENDING, ACCEPTED, REJECTED

    @Column(name = "application_date", nullable = false)
    private LocalDateTime applicationDate;

    // Constructors
    public InternshipApplication() {}

    public InternshipApplication(Intern intern, Company company, String status, LocalDateTime applicationDate) {
        this.intern = intern;
        this.company = company;
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

    public Intern getIntern() {
        return intern;
    }

    public void setIntern(Intern intern) {
        this.intern = intern;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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