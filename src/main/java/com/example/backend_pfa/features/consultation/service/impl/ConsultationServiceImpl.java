package com.example.backend_pfa.features.consultation.service.impl;

import com.example.backend_pfa.features.DTO.ConsultationDto;
import com.example.backend_pfa.features.RDV.dao.entities.Rdv;
import com.example.backend_pfa.features.RDV.dao.repository.RdvRepository;
import com.example.backend_pfa.features.consultation.dao.entities.Consultation;
import com.example.backend_pfa.features.consultation.dao.repository.ConsultationRepository;
import com.example.backend_pfa.features.consultation.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {

    private final RdvRepository rdvRepository;
    private final ConsultationRepository consultationRepository;

    @Override
    public ConsultationDto createConsultation(Long rdvId, String notes) {
        Rdv rdv = rdvRepository.findById(rdvId)
                .orElseThrow(() -> new RuntimeException("RDV not found"));

        Consultation consultation = Consultation.builder()
                .rdv(rdv)
                .notes(notes)
                .createdAt(LocalDateTime.now())
                .build();

        return toDto(consultationRepository.save(consultation));
    }

    @Override
    public ConsultationDto getByRdvId(Long rdvId) {
        Consultation consultation = consultationRepository.findByRdvId(rdvId)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));
        return toDto(consultation);
    }

    private ConsultationDto toDto(Consultation consultation) {
        return ConsultationDto.builder()
                .id(consultation.getId())
                .notes(consultation.getNotes())
                .createdAt(consultation.getCreatedAt())
                .rdvId(consultation.getRdv().getId())
                .build();
    }
}
