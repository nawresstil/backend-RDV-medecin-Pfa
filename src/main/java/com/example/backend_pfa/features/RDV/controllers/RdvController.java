package com.example.backend_pfa.features.RDV.controllers;


import com.example.backend_pfa.features.DTO.RdvDto;
import com.example.backend_pfa.features.RDV.service.RdvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rdv")
@RequiredArgsConstructor
public class RdvController {

    private final RdvService rdvService;

    @PostMapping("/create")
    public ResponseEntity<RdvDto> create(@RequestBody RdvDto dto) {
        return ResponseEntity.ok(rdvService.bookRdv(dto));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<RdvDto>> getByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(rdvService.getAllRdvForDoctor(doctorId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<RdvDto>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(rdvService.getAllRdvForPatient(patientId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<RdvDto> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(rdvService.updateStatus(id, status));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelRdv(@PathVariable Long id) {
        rdvService.cancelRdv(id);  // Call the cancelRdv method in the service
        return ResponseEntity.noContent().build();  // Return a 204 No Content response
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<RdvDto> confirmRdv(@PathVariable Long id) {
        RdvDto rdvDto = rdvService.confirmRdv(id);  // Call the confirmRdv method in the service
        return ResponseEntity.ok(rdvDto);  // Return the updated RDV as a response
    }

}
