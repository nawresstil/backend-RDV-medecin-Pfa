package com.example.backend_pfa.features.RDV.service.impl;

import com.example.backend_pfa.features.DTO.RdvDto;
import com.example.backend_pfa.features.RDV.dao.entities.Rdv;
import com.example.backend_pfa.features.RDV.dao.repository.RdvRepository;
import com.example.backend_pfa.features.RDV.enums.RdvStatus;
import com.example.backend_pfa.features.RDV.service.RdvService;
import com.example.backend_pfa.features.user.dao.entities.User;
import com.example.backend_pfa.features.user.dao.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RdvServiceImpl implements RdvService {

    private final RdvRepository rdvRepository;
    private final UserRepository userRepository;

    @Override
    public RdvDto bookRdv(RdvDto rdvDto) {
        User doctor = userRepository.findById(rdvDto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        User patient = userRepository.findById(rdvDto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Rdv rdv = Rdv.builder()
                .date(rdvDto.getDate())
                .startTime(rdvDto.getStartTime())
                .endTime(rdvDto.getEndTime())
                .status(RdvStatus.PENDING)
                .doctor(doctor)
                .patient(patient)
                .build();

        return toDto(rdvRepository.save(rdv));
    }

    @Override
    public List<RdvDto> getAllRdvForDoctor(Long doctorId) {
        return rdvRepository.findByDoctorId(doctorId).stream().map(this::toDto).toList();
    }

    @Override
    public List<RdvDto> getAllRdvForPatient(Long patientId) {
        return rdvRepository.findByPatientId(patientId).stream().map(this::toDto).toList();
    }

    @Override
    public void cancelRdv(Long rdvId) {
        Rdv rdv = rdvRepository.findById(rdvId)
                .orElseThrow(() -> new RuntimeException("RDV not found"));
        rdv.setStatus(RdvStatus.CANCELLED);
        rdvRepository.save(rdv);
    }

    @Override
    public RdvDto confirmRdv(Long rdvId) {
        Rdv rdv = rdvRepository.findById(rdvId)
                .orElseThrow(() -> new RuntimeException("RDV not found"));
        rdv.setStatus(RdvStatus.CONFIRMED);
        return toDto(rdvRepository.save(rdv));
    }

    private RdvDto toDto(Rdv rdv) {
        return RdvDto.builder()
                .id(rdv.getId())
                .date(rdv.getDate())
                .startTime(rdv.getStartTime())
                .endTime(rdv.getEndTime())
                .status(rdv.getStatus().name())
                .doctorId(rdv.getDoctor().getId())
                .patientId(rdv.getPatient().getId())
                .build();
    }


    @Override
    public RdvDto updateStatus(Long id, String status) {
        Rdv rdv = rdvRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RDV not found"));

        // Convert the status string to the corresponding RdvStatus enum
        RdvStatus rdvStatus = RdvStatus.valueOf(status.toUpperCase());
        rdv.setStatus(rdvStatus);

        // Save the updated RDV
        rdvRepository.save(rdv);

        // Return the updated RDV as a DTO
        return toDto(rdv);
    }
}
