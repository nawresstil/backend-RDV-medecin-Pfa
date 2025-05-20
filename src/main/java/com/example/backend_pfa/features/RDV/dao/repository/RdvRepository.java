package com.example.backend_pfa.features.RDV.dao.repository;


import com.example.backend_pfa.features.RDV.dao.entities.Rdv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RdvRepository extends JpaRepository<Rdv, Long> {
    List<Rdv> findByDoctorId(Long doctorId);
    List<Rdv> findByPatientId(Long patientId);
}

