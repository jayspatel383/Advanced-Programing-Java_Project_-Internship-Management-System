package com.internship.internship_management.repositories;

import com.internship.internship_management.entities.Company;
import com.internship.internship_management.entities.Intern;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    // Find company by name (for duplicate checking)
    Optional<Company> findByName(String name);

}