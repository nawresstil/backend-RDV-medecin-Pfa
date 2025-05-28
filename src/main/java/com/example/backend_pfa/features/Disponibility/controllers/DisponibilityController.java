package com.example.backend_pfa.features.Disponibility.controllers;

import com.example.backend_pfa.features.DTO.DisponibilityDto;
import com.example.backend_pfa.features.Disponibility.service.DisponibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disponibilities")
public class DisponibilityController {

    @Autowired
    private DisponibilityService disponibilityService;

    // Create new availability
    @PostMapping("/create")
    public ResponseEntity<?> createDisponibility(@RequestBody DisponibilityDto dto) {
        try {
            DisponibilityDto result = disponibilityService.createDisponibility(dto);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get all availabilities for a specific doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DisponibilityDto>> getDisponibilitiesByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(disponibilityService.getDisponibilitiesByDoctor(doctorId));
    }

    // Update a specific availability
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDisponibility(@PathVariable Long id, @RequestBody DisponibilityDto dto) {
        try {
            DisponibilityDto updated = disponibilityService.updateDisponibility(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete a specific availability
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDisponibility(@PathVariable Long id) {
        disponibilityService.deleteDisponibility(id);
        return ResponseEntity.noContent().build();
    }
}
