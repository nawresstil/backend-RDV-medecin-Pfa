package com.example.backend_pfa.features.RDV.dao.entities;

import com.example.backend_pfa.features.RDV.enums.RdvStatus;
import com.example.backend_pfa.features.consultation.dao.entities.Consultation;
import com.example.backend_pfa.features.user.dao.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rdv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;


    @Enumerated(EnumType.STRING)
    private RdvStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    private User doctor;

    @ManyToOne
    private User patient;

    @OneToOne(mappedBy = "rdv")
    private Consultation consultation;

}
