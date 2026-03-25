package com.internship.internship_management.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "skills")
    private List<Intern> interns;

    public Skill() {
    }

    public Skill(String name) {
        this.name = name;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public List<Intern> getInterns() { return interns; }

    public void setInterns(List<Intern> interns) { this.interns = interns; }
}
