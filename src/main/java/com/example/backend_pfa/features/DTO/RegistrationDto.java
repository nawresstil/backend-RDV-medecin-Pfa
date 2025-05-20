package com.example.backend_pfa.features.DTO;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class RegistrationDto {
    private String name;
    private int year;
}
