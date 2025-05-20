package com.example.backend_pfa.features.DTO;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class RdvDto {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status; // String instead of enum
}
