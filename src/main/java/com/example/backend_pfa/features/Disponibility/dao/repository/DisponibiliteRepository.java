package com.example.backend_pfa.features.Disponibility.dao.repository;

import com.example.backend_pfa.features.Disponibility.dao.entities.Disponibilite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Long> {
    List<Disponibilite> findByDoctorId(Long doctorId);
}

