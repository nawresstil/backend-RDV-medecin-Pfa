package com.example.backend_pfa.features.Speciality.dao.repository;

import com.example.backend_pfa.features.Speciality.dao.entities.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialityRepository extends JpaRepository<Speciality,Long> {
    Optional<Speciality> findByName(String name);

}
