package com.example.backend_pfa.features.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class ConsultationDto {
    private Long id;
    private Long rdvId;
    private String diagnosis;
    private String prescription;
    private String notes;
    private LocalDateTime createdAt;

}
