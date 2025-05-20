package com.example.backend_pfa.features.consultation.service;


import com.example.backend_pfa.features.DTO.ConsultationDto;

public interface ConsultationService {
    ConsultationDto createConsultation(Long rdvId, String notes);
    ConsultationDto getByRdvId(Long rdvId);
}

