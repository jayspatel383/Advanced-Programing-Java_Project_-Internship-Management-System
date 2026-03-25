package com.internship.internship_management.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location;

    @OneToMany(mappedBy = "company")
    private List<InternshipApplication> applications;

    public Company() {
    }

    public Company(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public List<InternshipApplication> getApplications() { return applications; }

    public void setApplications(List<InternshipApplication> applications) {
        this.applications = applications;
    }
}
