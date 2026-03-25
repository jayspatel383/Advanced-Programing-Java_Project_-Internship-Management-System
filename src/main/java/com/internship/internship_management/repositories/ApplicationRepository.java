package com.internship.internship_management.repositories;

import com.internship.internship_management.entities.InternshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<InternshipApplication, Long> {
    // Find applications by intern
    List<InternshipApplication> findByInternId(Long internId);

    // Find applications by company
    List<InternshipApplication> findByCompanyId(Long companyId);

    // Find applications by status
    List<InternshipApplication> findByStatus(String status);

    // Check if an intern already applied to a company
    Optional<InternshipApplication> findByInternIdAndCompanyId(Long internId, Long companyId);

    // Find applications by intern and status
    List<InternshipApplication> findByInternIdAndStatus(Long internId, String status);

    // Find applications by company and status
    List<InternshipApplication> findByCompanyIdAndStatus(Long companyId, String status);
}