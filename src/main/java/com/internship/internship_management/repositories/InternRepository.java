package com.internship.internship_management.repositories;

import com.internship.internship_management.entities.Intern;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InternRepository extends JpaRepository<Intern, Long> {
    // Add this method to find by email
    Optional<Intern> findByEmail(String email);

    Optional<Intern> findById(Long aLong);
}