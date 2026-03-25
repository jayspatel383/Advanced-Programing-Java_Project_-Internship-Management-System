package com.internship.internship_management.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Intern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String university;

    @ManyToMany
    @JoinTable(
            name = "intern_skills",
            joinColumns = @JoinColumn(name = "intern_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();

    // Constructors
    public Intern() {
    }

    public Intern(String name, String email, String university) {
        this.name = name;
        this.email = email;
        this.university = university;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getUniversity() { return university; }

    public void setUniversity(String university) { this.university = university; }

    public List<Skill> getSkills() { return skills; }

    public void setSkills(List<Skill> skills) { this.skills = skills; }
}
