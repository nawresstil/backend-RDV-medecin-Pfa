package com.example.backend_pfa.features.DTO;

import jakarta.persistence.Embeddable;
import lombok.Data;
@Embeddable
@Data
public class ExperienceDto {
    private String hospitalName;
    private String designation;
    private String fromDate; // renamed from 'from'
    private String toDate;   // renamed from 'to'
}
