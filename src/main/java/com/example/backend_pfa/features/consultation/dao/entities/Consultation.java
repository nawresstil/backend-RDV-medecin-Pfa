package com.example.backend_pfa.features.consultation.dao.entities;

import com.example.backend_pfa.features.RDV.dao.entities.Rdv;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String notes;

    private LocalDateTime createdAt;

    @OneToOne
    private Rdv rdv;
}
