package com.example.backend_pfa.features.Disponibility.service;


import com.example.backend_pfa.features.DTO.DisponibiliteDto;

import java.util.List;

public interface DisponibiliteService {
    DisponibiliteDto addDisponibilite(DisponibiliteDto dto);
    DisponibiliteDto updateDisponibilite(Long id, DisponibiliteDto dto);
    void deleteDisponibilite(Long id);
    List<DisponibiliteDto> getDisponibilitesByDoctor(Long doctorId);
}
