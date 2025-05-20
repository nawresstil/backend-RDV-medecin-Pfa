package com.example.backend_pfa.features.Disponibility.service.impl;

import com.example.backend_pfa.features.DTO.DisponibiliteDto;
import com.example.backend_pfa.features.Disponibility.dao.entities.Disponibilite;
import com.example.backend_pfa.features.Disponibility.dao.repository.DisponibiliteRepository;
import com.example.backend_pfa.features.Disponibility.service.DisponibiliteService;
import com.example.backend_pfa.features.user.dao.entities.User;
import com.example.backend_pfa.features.user.dao.repositories.UserRepository;
import com.example.backend_pfa.features.user.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisponibiliteServiceImpl implements DisponibiliteService {

    @Autowired
    private DisponibiliteRepository disponibiliteRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public DisponibiliteDto addDisponibilite(DisponibiliteDto dto) {
        User doctor = userRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor (User) not found"));

        if (doctor.getRole() != Role.DOCTOR) {
            throw new RuntimeException("User is not a doctor");
        }

        Disponibilite d = new Disponibilite();
        d.setDay(dto.getDay());
        d.setStartTime(dto.getStartTime());
        d.setEndTime(dto.getEndTime());
        d.setCreatedDate(LocalDate.now());
        d.setDoctor(doctor);
        d.setReserved(false);

        return toDto(disponibiliteRepository.save(d));
    }

    @Override
    public DisponibiliteDto updateDisponibilite(Long id, DisponibiliteDto dto) {
        Disponibilite d = disponibiliteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilité not found"));

        d.setStartTime(dto.getStartTime());
        d.setEndTime(dto.getEndTime());
        d.setUpdatedDate(LocalDate.now());

        return toDto(disponibiliteRepository.save(d));
    }

    @Override
    public void deleteDisponibilite(Long id) {
        Disponibilite disponibilite = disponibiliteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilité introuvable"));
        disponibiliteRepository.delete(disponibilite);
    }



    @Override
    public List<DisponibiliteDto> getDisponibilitesByDoctor(Long doctorId) {
        return disponibiliteRepository.findByDoctorId(doctorId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private DisponibiliteDto toDto(Disponibilite d) {
        DisponibiliteDto dto = new DisponibiliteDto();
        dto.setId(d.getId());
        dto.setDay(d.getDay());
        dto.setStartTime(d.getStartTime());
        dto.setEndTime(d.getEndTime());
        dto.setReserved(d.isReserved());
        dto.setDoctorId(d.getDoctor().getId());
        return dto;
    }
}
