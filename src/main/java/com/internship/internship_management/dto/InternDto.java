package com.internship.internship_management.dto;

import java.util.List;

public class InternDto {

    private Long id;
    private String name;
    private String email;
    private String university;
    private List<SkillDto> skills;  // List of skills (not IDs)

    // Constructors
    public InternDto() {}

    public InternDto(Long id, String name, String email, String university, List<SkillDto> skills) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.university = university;
        this.skills = skills;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public List<SkillDto> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillDto> skills) {
        this.skills = skills;
    }
}