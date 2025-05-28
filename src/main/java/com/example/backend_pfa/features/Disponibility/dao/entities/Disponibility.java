package com.example.backend_pfa.features.Disponibility.dao.entities;

import com.example.backend_pfa.features.user.dao.entities.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Disponibility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dayOfWeek; // e.g. "Monday"
    private String startTime; // e.g. "08:00"
    private String endTime;   // e.g. "12:00"
    @Column(name = "slot_duration_minutes")
    private Integer slotDurationMinutes;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;


}
