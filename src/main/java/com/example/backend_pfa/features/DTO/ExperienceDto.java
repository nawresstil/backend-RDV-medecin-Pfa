package com.example.backend_pfa.features.DTO;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ExperienceDto {
    private String hospitalName;
    private String designation;
    private String from;
    private String to;
}
