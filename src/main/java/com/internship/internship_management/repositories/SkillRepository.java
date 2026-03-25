package com.internship.internship_management.repositories;

import com.internship.internship_management.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    // Add this method to find by name
    Optional<Skill> findByName(String name);


}