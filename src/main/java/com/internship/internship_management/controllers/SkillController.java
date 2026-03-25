package com.internship.internship_management.controllers;

import com.internship.internship_management.dto.SkillDto;
import com.internship.internship_management.entities.Skill;
import com.internship.internship_management.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/skills")
public class SkillController {

    @Autowired
    private SkillRepository skillRepository;

    // Helper method to convert Entity to DTO
    private SkillDto convertToDto(Skill skill) {
        return new SkillDto(skill.getId(), skill.getName());
    }

    // Helper method to convert DTO to Entity
    private Skill convertToEntity(SkillDto dto) {
        Skill skill = new Skill();
        skill.setName(dto.getName());
        return skill;
    }

    // CREATE - POST a new skill
    @PostMapping
    public ResponseEntity<?> createSkill(@RequestBody SkillDto skillDto) {
        try {
            // Check if skill with same name already exists
            Optional<Skill> existingSkill = skillRepository.findByName(skillDto.getName());
            if (existingSkill.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Skill with name '" + skillDto.getName() + "' already exists");
            }

            // Convert DTO to Entity and save
            Skill skill = convertToEntity(skillDto);
            Skill savedSkill = skillRepository.save(skill);

            // Return saved entity as DTO
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertToDto(savedSkill));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating skill: " + e.getMessage());
        }
    }

    // READ ALL - GET all skills
    @GetMapping
    public ResponseEntity<?> getAllSkills() {
        try {
            List<Skill> skills = skillRepository.findAll();
            List<SkillDto> skillDtos = skills.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(skillDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching skills: " + e.getMessage());
        }
    }

    // READ ONE - GET skill by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSkillById(@PathVariable Long id) {
        try {
            Optional<Skill> skillOpt = skillRepository.findById(id);

            if (skillOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Skill not found with id: " + id);
            }

            return ResponseEntity.ok(convertToDto(skillOpt.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching skill: " + e.getMessage());
        }
    }

    // UPDATE - PUT to update a skill
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSkill(@PathVariable Long id, @RequestBody SkillDto skillDto) {
        try {
            Optional<Skill> existingSkillOpt = skillRepository.findById(id);

            if (existingSkillOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Skill not found with id: " + id);
            }

            Skill skillToUpdate = existingSkillOpt.get();
            skillToUpdate.setName(skillDto.getName());

            Skill updatedSkill = skillRepository.save(skillToUpdate);
            return ResponseEntity.ok(convertToDto(updatedSkill));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating skill: " + e.getMessage());
        }
    }

    // DELETE - DELETE a skill
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSkill(@PathVariable Long id) {
        try {
            Optional<Skill> skillOpt = skillRepository.findById(id);

            if (skillOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Skill not found with id: " + id);
            }

            skillRepository.deleteById(id);
            return ResponseEntity.ok("Skill deleted successfully with id: " + id);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting skill: " + e.getMessage());
        }
    }
}