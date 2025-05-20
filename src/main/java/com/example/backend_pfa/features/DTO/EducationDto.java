package com.example.backend_pfa.features.DTO;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class EducationDto {
    private String degree;
    private String institute;
    private int yearOfCompletion;
}