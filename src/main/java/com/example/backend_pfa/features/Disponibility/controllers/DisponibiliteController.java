package com.example.backend_pfa.features.Disponibility.controllers;

import com.example.backend_pfa.features.DTO.DisponibiliteDto;
import com.example.backend_pfa.features.Disponibility.service.DisponibiliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/disponibility")
public class DisponibiliteController {

    @Autowired
    private DisponibiliteService disponibiliteService;

    @PostMapping("/create")
    public ResponseEntity<DisponibiliteDto> create(@RequestBody DisponibiliteDto dto) {
        return ResponseEntity.ok(disponibiliteService.addDisponibilite(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisponibiliteDto> update(@PathVariable Long id, @RequestBody DisponibiliteDto dto) {
        return ResponseEntity.ok(disponibiliteService.updateDisponibilite(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        disponibiliteService.deleteDisponibilite(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Disponibilité supprimée avec succès");

        return ResponseEntity.ok(response); // HTTP 200 with JSON message
    }


    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DisponibiliteDto>> getByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(disponibiliteService.getDisponibilitesByDoctor(doctorId));
    }
}
