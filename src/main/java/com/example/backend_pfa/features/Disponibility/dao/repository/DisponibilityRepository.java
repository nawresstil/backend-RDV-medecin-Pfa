package com.example.backend_pfa.features.Disponibility.dao.repository;


import com.example.backend_pfa.features.Disponibility.dao.entities.Disponibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilityRepository extends JpaRepository<Disponibility, Long> {
    List<Disponibility> findByDoctorId(Long doctorId);
    List<Disponibility> findByDoctorIdAndDayOfWeek(Long doctorId, String dayOfWeek);

}


