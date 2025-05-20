package com.example.backend_pfa.features.consultation.controllers;

import com.example.backend_pfa.features.DTO.ConsultationDto;
import com.example.backend_pfa.features.consultation.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultation")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;

    @PostMapping("/create")
    public ResponseEntity<ConsultationDto> create(@RequestBody ConsultationDto dto) {
        return ResponseEntity.ok(consultationService.createConsultation(dto.getRdvId(), dto.getNotes()));
    }

    @GetMapping("/rdv/{rdvId}")
    public ResponseEntity<ConsultationDto> getByRdv(@PathVariable Long rdvId) {
        return ResponseEntity.ok(consultationService.getByRdvId(rdvId));
    }
}
