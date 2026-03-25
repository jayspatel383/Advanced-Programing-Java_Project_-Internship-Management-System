package com.internship.internship_management.controllers;
import com.internship.internship_management.dto.ActivityLogDto;
import com.internship.internship_management.entities.ActivityLog;
import com.internship.internship_management.entities.Intern;
import com.internship.internship_management.repositories.ActivityLogRepository;
import com.internship.internship_management.repositories.InternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/activitylogs")
public class ActivityLogController {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private InternRepository internRepository;

    // Helper: Converting Entity to DTO
    private ActivityLogDto convertToDto(ActivityLog log) {
        return new ActivityLogDto(
                log.getId(),
                log.getIntern().getId(),
                log.getAction(),
                log.getTimestamp()
        );
    }

    // creating - Post a new activity log
    @PostMapping
    public ResponseEntity<?> createActivityLog(@RequestBody ActivityLogDto dto) {
        try {
            System.out.println("Received request for internId: " + dto.getInternId());
            System.out.println("Action: " + dto.getAction());

            Optional<Intern> internOpt = internRepository.findById(dto.getInternId());

            if (internOpt.isEmpty()) {
                String errorMsg = "Intern not found with id: " + dto.getInternId();
                System.out.println("ERROR: " + errorMsg);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(errorMsg);
            }

            Intern intern = internOpt.get();
            System.out.println("Found intern: " + intern.getId() + " - " + intern.getName());

            ActivityLog log = new ActivityLog();
            log.setAction(dto.getAction());
            log.setTimestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now());
            log.setIntern(intern);

            ActivityLog saved = activityLogRepository.save(log);
            System.out.println("Saved activity log with id: " + saved.getId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertToDto(saved));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating activity log: " + e.getMessage());
        }
    }

    @GetMapping("/debug/intern/{internId}")
    public ResponseEntity<?> debugByInternId(@PathVariable Long internId) {
        List<ActivityLog> logs = activityLogRepository.findByInternId(internId);
        return ResponseEntity.ok("Found " + logs.size() + " logs for intern " + internId);
    }

    // READ ALL - Getting all activity logs
    @GetMapping
    public ResponseEntity<?> getAllActivityLogs() {
        try {
            List<ActivityLog> logs = activityLogRepository.findAll();
            List<ActivityLogDto> logDtos = logs.stream()
                    .map(this::convertToDto)
                    .toList();
            return ResponseEntity.ok(logDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching activity logs: " + e.getMessage());
        }
    }

    // READ ONE - Gets activity log by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getActivityLogById(@PathVariable Long id) {
        try {
            Optional<ActivityLog> logOpt = activityLogRepository.findById(id);

            if (logOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Activity log not found with id: " + id);
            }

            return ResponseEntity.ok(convertToDto(logOpt.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching activity log: " + e.getMessage());
        }
    }

    // Reading by id - GET all logs for a specific intern
    @GetMapping("/intern/{internId}")
    public ResponseEntity<?> getActivityLogsByInternId(@PathVariable Long internId) {
        try {
            // First check if intern exists
            Optional<Intern> internOpt = internRepository.findById(internId);
            if (internOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Intern not found with id: " + internId);
            }

            // Find ALL logs for this intern
            List<ActivityLog> logs = activityLogRepository.findByInternId(internId);

            if (logs.isEmpty()) {
                return ResponseEntity.ok("No activity logs found for intern with id: " + internId);
            }

            List<ActivityLogDto> logDtos = logs.stream()
                    .map(this::convertToDto)
                    .toList();

            return ResponseEntity.ok(logDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching activity logs: " + e.getMessage());
        }
    }

    // Updating - PUT to update an activity log
    @PutMapping("/{id}")
    public ResponseEntity<?> updateActivityLog(@PathVariable Long id, @RequestBody ActivityLogDto dto) {
        try {
            Optional<ActivityLog> existingLogOpt = activityLogRepository.findById(id);

            if (existingLogOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Activity log not found with id: " + id);
            }

            // Verify intern exists if internId is being changed
            if (dto.getInternId() != null) {
                Optional<Intern> internOpt = internRepository.findById(dto.getInternId());
                if (internOpt.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Intern not found with id: " + dto.getInternId());
                }
            }

            ActivityLog logToUpdate = existingLogOpt.get();

            // Update fields if provided
            if (dto.getAction() != null) {
                logToUpdate.setAction(dto.getAction());
            }

            if (dto.getTimestamp() != null) {
                logToUpdate.setTimestamp(dto.getTimestamp());
            }

            if (dto.getInternId() != null) {
                Intern intern = internRepository.findById(dto.getInternId()).get();
                logToUpdate.setIntern(intern);
            }

            ActivityLog updatedLog = activityLogRepository.save(logToUpdate);
            return ResponseEntity.ok(convertToDto(updatedLog));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating activity log: " + e.getMessage());
        }
    }

    // DELETE - Deleting an activity log
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteActivityLog(@PathVariable Long id) {
        try {
            Optional<ActivityLog> logOpt = activityLogRepository.findById(id);

            if (logOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Activity log not found with id: " + id);
            }

            activityLogRepository.deleteById(id);
            return ResponseEntity.ok("Activity log deleted successfully with id: " + id);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting activity log: " + e.getMessage());
        }
    }

    // DELETE ALL BY INTERN - Delete all logs for a specific intern
    @DeleteMapping("/intern/{internId}")
    public ResponseEntity<?> deleteAllLogsByInternId(@PathVariable Long internId) {
        try {
            // Check if intern exists
            Optional<Intern> internOpt = internRepository.findById(internId);
            if (internOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Intern not found with id: " + internId);
            }

            List<ActivityLog> logs = activityLogRepository.findByInternId(internId);
            if (logs.isEmpty()) {
                return ResponseEntity.ok("No activity logs found for intern with id: " + internId);
            }

            activityLogRepository.deleteAll(logs);
            return ResponseEntity.ok("All activity logs deleted for intern with id: " + internId);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting activity logs: " + e.getMessage());
        }
    }
}