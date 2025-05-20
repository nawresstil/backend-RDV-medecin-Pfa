package com.example.backend_pfa.features.consultation.dao.repository;


import com.example.backend_pfa.features.consultation.dao.entities.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    Optional<Consultation> findByRdvId(Long rdvId);

}
