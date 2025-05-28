package com.example.backend_pfa.features.DTO;

import lombok.Data;

import java.util.List;

@Data
public class DisponibilityDto {

    private Long id;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private Integer slotDurationMinutes;
    private List<String> generatedSlots;
    private Long doctorId;
}

