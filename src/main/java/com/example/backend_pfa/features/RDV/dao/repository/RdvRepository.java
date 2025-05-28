package com.example.backend_pfa.features.RDV.dao.repository;


import com.example.backend_pfa.features.RDV.dao.entities.Rdv;
import com.example.backend_pfa.features.RDV.enums.RdvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RdvRepository extends JpaRepository<Rdv, Long> {
    List<Rdv> findByDoctorId(Long doctorId);
    List<Rdv> findByPatientId(Long patientId);
    boolean existsByDoctorIdAndDateAndStartTimeAndEndTimeAndStatusIn(
            Long doctorId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            List<RdvStatus> statuses
    );

    @Query("""
    SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
    FROM Rdv r
    WHERE r.doctor.id = :doctorId
    AND r.date = :date
    AND r.status IN :statuses
    AND (
        (r.startTime < :endTime AND r.endTime > :startTime)
    )
""")
    boolean hasTimeConflict(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("statuses") List<RdvStatus> statuses
    );


}

