package com.internship.internship_management.repositories;

import com.internship.internship_management.entities.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    // Find all logs for a specific intern
    List<ActivityLog> findByInternId(Long internId);
}