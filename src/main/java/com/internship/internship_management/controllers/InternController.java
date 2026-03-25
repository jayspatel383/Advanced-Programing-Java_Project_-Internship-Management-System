package com.internship.internship_management.controllers;

import com.internship.internship_management.dto.InternDto;
import com.internship.internship_management.dto.SkillDto;
import com.internship.internship_management.entities.Intern;
import com.internship.internship_management.entities.Skill;
import com.internship.internship_management.repositories.InternRepository;
import com.internship.internship_management.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/interns")
public class InternController {

    @Autowired
    private InternRepository internRepository;

    @Autowired
    private SkillRepository skillRepository;

    // Helper: Convert Entity to DTO
    private InternDto convertToDto(Intern intern) {
        List<SkillDto> skillDtos = intern.getSkills().stream()
                .map(skill -> new SkillDto(skill.getId(), skill.getName()))
                .collect(Collectors.toList());

        return new InternDto(
                intern.getId(),
                intern.getName(),
                intern.getEmail(),
                intern.getUniversity(),
                skillDtos
        );
    }

    // Helper: Convert DTO to Entity (without skills)
    private Intern convertToEntity(InternDto dto) {
        Intern intern = new Intern();
        intern.setName(dto.getName());
        intern.setEmail(dto.getEmail());
        intern.setUniversity(dto.getUniversity());
        return intern;
    }

    // CREATE - POST a new intern
    @PostMapping
    public ResponseEntity<?> createIntern(@RequestBody InternDto internDto) {
        try {
            // Check if email already exists
            Optional<Intern> existingIntern = internRepository.findByEmail(internDto.getEmail());
            if (existingIntern.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Intern with email '" + internDto.getEmail() + "' already exists");
            }

            // Create intern without skills first
            Intern intern = convertToEntity(internDto);

            // Add skills if provided
            if (internDto.getSkills() != null && !internDto.getSkills().isEmpty()) {
                List<Skill> skills = new ArrayList<>();
                for (SkillDto skillDto : internDto.getSkills()) {
                    Optional<Skill> skillOpt = skillRepository.findById(skillDto.getId());
                    if (skillOpt.isPresent()) {
                        skills.add(skillOpt.get());
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Skill not found with id: " + skillDto.getId());
                    }
                }
                intern.setSkills(skills);
            }

            Intern savedIntern = internRepository.save(intern);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertToDto(savedIntern));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating intern: " + e.getMessage());
        }
    }

    // READ ALL - GET all interns
    @GetMapping
    public ResponseEntity<?> getAllInterns() {
        try {
            List<Intern> interns = internRepository.findAll();
            List<InternDto> internDtos = interns.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(internDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching interns: " + e.getMessage());
        }
    }

    // READ ONE - GET intern by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getInternById(@PathVariable Long id) {
        try {
            Optional<Intern> internOpt = internRepository.findById(id);

            if (internOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Intern not found with id: " + id);
            }

            return ResponseEntity.ok(convertToDto(internOpt.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching intern: " + e.getMessage());
        }
    }

    // UPDATE - PUT to update an intern
    @PutMapping("/{id}")
    public ResponseEntity<?> updateIntern(@PathVariable Long id, @RequestBody InternDto internDto) {
        try {
            Optional<Intern> existingInternOpt = internRepository.findById(id);

            if (existingInternOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Intern not found with id: " + id);
            }

            Intern internToUpdate = existingInternOpt.get();
            internToUpdate.setName(internDto.getName());
            internToUpdate.setEmail(internDto.getEmail());
            internToUpdate.setUniversity(internDto.getUniversity());

            // Update skills if provided
            if (internDto.getSkills() != null) {
                List<Skill> skills = new ArrayList<>();
                for (SkillDto skillDto : internDto.getSkills()) {
                    Optional<Skill> skillOpt = skillRepository.findById(skillDto.getId());
                    if (skillOpt.isPresent()) {
                        skills.add(skillOpt.get());
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Skill not found with id: " + skillDto.getId());
                    }
                }
                internToUpdate.setSkills(skills);
            }

            Intern updatedIntern = internRepository.save(internToUpdate);
            return ResponseEntity.ok(convertToDto(updatedIntern));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating intern: " + e.getMessage());
        }
    }

    // DELETE - DELETE an intern
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIntern(@PathVariable Long id) {
        try {
            Optional<Intern> internOpt = internRepository.findById(id);

            if (internOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Intern not found with id: " + id);
            }

            internRepository.deleteById(id);
            return ResponseEntity.ok("Intern deleted successfully with id: " + id);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting intern: " + e.getMessage());
        }
    }

    // CUSTOM ENDPOINT: Add skill to intern
    @PostMapping("/{internId}/skills/{skillId}")
    public ResponseEntity<?> addSkillToIntern(@PathVariable Long internId, @PathVariable Long skillId) {
        try {
            Optional<Intern> internOpt = internRepository.findById(internId);
            Optional<Skill> skillOpt = skillRepository.findById(skillId);

            if (internOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Intern not found with id: " + internId);
            }

            if (skillOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Skill not found with id: " + skillId);
            }

            Intern intern = internOpt.get();
            Skill skill = skillOpt.get();

            intern.getSkills().add(skill);
            Intern updatedIntern = internRepository.save(intern);

            return ResponseEntity.ok(convertToDto(updatedIntern));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding skill to intern: " + e.getMessage());
        }
    }

    // CUSTOM ENDPOINT: Remove skill from intern
    @DeleteMapping("/{internId}/skills/{skillId}")
    public ResponseEntity<?> removeSkillFromIntern(@PathVariable Long internId, @PathVariable Long skillId) {
        try {
            Optional<Intern> internOpt = internRepository.findById(internId);
            Optional<Skill> skillOpt = skillRepository.findById(skillId);

            if (internOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Intern not found with id: " + internId);
            }

            if (skillOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Skill not found with id: " + skillId);
            }

            Intern intern = internOpt.get();
            Skill skill = skillOpt.get();

            intern.getSkills().remove(skill);
            Intern updatedIntern = internRepository.save(intern);

            return ResponseEntity.ok(convertToDto(updatedIntern));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error removing skill from intern: " + e.getMessage());
        }
    }
}