package com.internship.internship_management.controllers;

import com.internship.internship_management.dto.ApplicationDto;
import com.internship.internship_management.entities.Company;
import com.internship.internship_management.entities.Intern;
import com.internship.internship_management.entities.InternshipApplication;
import com.internship.internship_management.repositories.ApplicationRepository;
import com.internship.internship_management.repositories.CompanyRepository;
import com.internship.internship_management.repositories.InternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private InternRepository internRepository;

    @Autowired
    private CompanyRepository companyRepository;

    // Helper: Convert Entity to DTO
    private ApplicationDto convertToDto(InternshipApplication application) {
        return new ApplicationDto(
                application.getId(),
                application.getIntern().getId(),
                application.getCompany().getId(),
                application.getStatus(),
                application.getApplicationDate()
        );
    }

    // Helper: Validate status
    private boolean isValidStatus(String status) {
        return status != null && (
                status.equals("PENDING") ||
                        status.equals("ACCEPTED") ||
                        status.equals("REJECTED")
        );
    }

    // CREATE - POST a new application
    @PostMapping
    public ResponseEntity<?> createApplication(@RequestBody ApplicationDto dto) {
        try {
            // Validate status
            if (!isValidStatus(dto.getStatus())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid status. Must be PENDING, ACCEPTED, or REJECTED");
            }

            // Check if intern exists
            Optional<Intern> internOpt = internRepository.findById(dto.getInternId());
            if (internOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Intern not found with id: " + dto.getInternId());
            }

            // Check if company exists
            Optional<Company> companyOpt = companyRepository.findById(dto.getCompanyId());
            if (companyOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Company not found with id: " + dto.getCompanyId());
            }

            // Check if application already exists (prevent duplicate)
            Optional<InternshipApplication> existingApp = applicationRepository
                    .findByInternIdAndCompanyId(dto.getInternId(), dto.getCompanyId());

            if (existingApp.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Intern already applied to this company");
            }

            // Create new application
            InternshipApplication application = new InternshipApplication();
            application.setIntern(internOpt.get());
            application.setCompany(companyOpt.get());
            application.setStatus(dto.getStatus());
            application.setApplicationDate(dto.getApplicationDate() != null ?
                    dto.getApplicationDate() : LocalDateTime.now());

            InternshipApplication savedApp = applicationRepository.save(application);

            // Also create an activity log for this application
            createActivityLog(internOpt.get(), "Applied to " + companyOpt.get().getName());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertToDto(savedApp));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating application: " + e.getMessage());
        }
    }

    // READ ALL - GET all applications
    @GetMapping
    public ResponseEntity<?> getAllApplications() {
        try {
            List<InternshipApplication> applications = applicationRepository.findAll();
            List<ApplicationDto> appDtos = applications.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(appDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching applications: " + e.getMessage());
        }
    }

    // READ ONE - GET application by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getApplicationById(@PathVariable Long id) {
        try {
            Optional<InternshipApplication> appOpt = applicationRepository.findById(id);

            if (appOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Application not found with id: " + id);
            }

            return ResponseEntity.ok(convertToDto(appOpt.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching application: " + e.getMessage());
        }
    }

    // GET applications by intern
    @GetMapping("/intern/{internId}")
    public ResponseEntity<?> getApplicationsByIntern(@PathVariable Long internId) {
        try {
            // Check if intern exists
            Optional<Intern> internOpt = internRepository.findById(internId);
            if (internOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Intern not found with id: " + internId);
            }

            List<InternshipApplication> applications = applicationRepository.findByInternId(internId);
            List<ApplicationDto> appDtos = applications.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            if (appDtos.isEmpty()) {
                return ResponseEntity.ok("No applications found for intern with id: " + internId);
            }

            return ResponseEntity.ok(appDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching applications: " + e.getMessage());
        }
    }

    // GET applications by company
    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> getApplicationsByCompany(@PathVariable Long companyId) {
        try {
            // Check if company exists
            Optional<Company> companyOpt = companyRepository.findById(companyId);
            if (companyOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Company not found with id: " + companyId);
            }

            List<InternshipApplication> applications = applicationRepository.findByCompanyId(companyId);
            List<ApplicationDto> appDtos = applications.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            if (appDtos.isEmpty()) {
                return ResponseEntity.ok("No applications found for company with id: " + companyId);
            }

            return ResponseEntity.ok(appDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching applications: " + e.getMessage());
        }
    }

    // GET applications by status
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getApplicationsByStatus(@PathVariable String status) {
        try {
            if (!isValidStatus(status)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid status. Must be PENDING, ACCEPTED, or REJECTED");
            }

            List<InternshipApplication> applications = applicationRepository.findByStatus(status);
            List<ApplicationDto> appDtos = applications.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(appDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching applications: " + e.getMessage());
        }
    }

    // UPDATE - PUT methode to update application status
    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplicationStatus(@PathVariable Long id, @RequestBody ApplicationDto dto) {
        try {
            Optional<InternshipApplication> existingAppOpt = applicationRepository.findById(id);

            if (existingAppOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Application not found with id: " + id);
            }

            // Validate status if provided
            if (dto.getStatus() != null && !isValidStatus(dto.getStatus())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid status. Must be PENDING, ACCEPTED, or REJECTED");
            }

            InternshipApplication appToUpdate = existingAppOpt.get();
            String oldStatus = appToUpdate.getStatus();

            // Update status if provided
            if (dto.getStatus() != null) {
                appToUpdate.setStatus(dto.getStatus());
            }

            // Update date if provided
            if (dto.getApplicationDate() != null) {
                appToUpdate.setApplicationDate(dto.getApplicationDate());
            }

            InternshipApplication updatedApp = applicationRepository.save(appToUpdate);

            // Create activity log for status change
            if (dto.getStatus() != null && !oldStatus.equals(dto.getStatus())) {
                createActivityLog(appToUpdate.getIntern(),
                        "Application status changed from " + oldStatus + " to " + dto.getStatus() +
                                " for " + appToUpdate.getCompany().getName());
            }

            return ResponseEntity.ok(convertToDto(updatedApp));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating application: " + e.getMessage());
        }
    }

    // Delete methode - Deletes an application
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id) {
        try {
            Optional<InternshipApplication> appOpt = applicationRepository.findById(id);

            if (appOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Application not found with id: " + id);
            }

            InternshipApplication app = appOpt.get();
            String companyName = app.getCompany().getName();

            applicationRepository.deleteById(id);

            // Create activity log for withdrawal
            createActivityLog(app.getIntern(), "Withdrew application from " + companyName);

            return ResponseEntity.ok("Application deleted successfully with id: " + id);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting application: " + e.getMessage());
        }
    }

    // Helper method to create activity logs
    private void createActivityLog(Intern intern, String action) {
        try {
            // You can autowire ActivityLogRepository and create log here
            // Or leave this as a placeholder for now
            System.out.println("ACTIVITY LOG: Intern " + intern.getName() + " - " + action);
        } catch (Exception e) {
            // Just log error but don't fail the main operation
            System.err.println("Failed to create activity log: " + e.getMessage());
        }
    }
}