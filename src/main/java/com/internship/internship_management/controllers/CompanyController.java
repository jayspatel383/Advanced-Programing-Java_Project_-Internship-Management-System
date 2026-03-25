package com.internship.internship_management.controllers;

import com.internship.internship_management.dto.CompanyDto;
import com.internship.internship_management.entities.Company;
import com.internship.internship_management.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    // Helper: Convert Entity to DTO
    private CompanyDto convertToDto(Company company) {
        return new CompanyDto(
                company.getId(),
                company.getName(),
                company.getLocation()
        );
    }

    // Helper: Convert DTO to Entity
    private Company convertToEntity(CompanyDto dto) {
        Company company = new Company();
        company.setName(dto.getName());
        company.setLocation(dto.getLocation());
        return company;
    }

    // CREATE - POST a new company
    @PostMapping
    public ResponseEntity<?> createCompany(@RequestBody CompanyDto companyDto) {
        try {
            // Check if company with same name already exists
            Optional<Company> existingCompany = companyRepository.findByName(companyDto.getName());
            if (existingCompany.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Company with name '" + companyDto.getName() + "' already exists");
            }

            // Convert DTO to Entity and save
            Company company = convertToEntity(companyDto);
            Company savedCompany = companyRepository.save(company);

            // Return saved entity as DTO
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertToDto(savedCompany));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating company: " + e.getMessage());
        }
    }

    // READ ALL - GET all companies
    @GetMapping
    public ResponseEntity<?> getAllCompanies() {
        try {
            List<Company> companies = companyRepository.findAll();
            List<CompanyDto> companyDtos = companies.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(companyDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching companies: " + e.getMessage());
        }
    }

    // READ ONE - GET company by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        try {
            Optional<Company> companyOpt = companyRepository.findById(id);

            if (companyOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Company not found with id: " + id);
            }

            return ResponseEntity.ok(convertToDto(companyOpt.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching company: " + e.getMessage());
        }
    }

    // UPDATE - PUT to update a company
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody CompanyDto companyDto) {
        try {
            Optional<Company> existingCompanyOpt = companyRepository.findById(id);

            if (existingCompanyOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Company not found with id: " + id);
            }

            // Check if another company with the same name exists (excluding this one)
            Optional<Company> companyWithSameName = companyRepository.findByName(companyDto.getName());
            if (companyWithSameName.isPresent() && !companyWithSameName.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Another company with name '" + companyDto.getName() + "' already exists");
            }

            Company companyToUpdate = existingCompanyOpt.get();
            companyToUpdate.setName(companyDto.getName());
            companyToUpdate.setLocation(companyDto.getLocation());

            Company updatedCompany = companyRepository.save(companyToUpdate);
            return ResponseEntity.ok(convertToDto(updatedCompany));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating company: " + e.getMessage());
        }
    }

    // DELETE - DELETE a company
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        try {
            Optional<Company> companyOpt = companyRepository.findById(id);

            if (companyOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Company not found with id: " + id);
            }

            companyRepository.deleteById(id);
            return ResponseEntity.ok("Company deleted successfully with id: " + id);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting company: " + e.getMessage());
        }
    }
}