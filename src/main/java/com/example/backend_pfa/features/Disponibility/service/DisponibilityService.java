package com.example.backend_pfa.features.Disponibility.service;


import com.example.backend_pfa.features.DTO.DisponibilityDto;

import java.util.List;

public interface DisponibilityService {

    DisponibilityDto createDisponibility(DisponibilityDto dto);

    List<DisponibilityDto> getDisponibilitiesByDoctor(Long doctorId);

    void deleteDisponibility(Long id);

    DisponibilityDto updateDisponibility(Long id, DisponibilityDto dto);

}
