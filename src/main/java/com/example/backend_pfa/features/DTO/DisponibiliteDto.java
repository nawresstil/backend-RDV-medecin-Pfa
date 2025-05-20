package com.example.backend_pfa.features.DTO;

import lombok.Data;

import java.time.LocalTime;

@Data
public class DisponibiliteDto {
    private Long id;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean reserved;
    private Long doctorId;
}
