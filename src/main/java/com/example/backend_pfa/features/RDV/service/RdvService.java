package com.example.backend_pfa.features.RDV.service;


import com.example.backend_pfa.features.DTO.RdvDto;

import java.util.List;

public interface RdvService {
    RdvDto bookRdv(RdvDto rdvDto);
    List<RdvDto> getAllRdvForDoctor(Long doctorId);
    List<RdvDto> getAllRdvForPatient(Long patientId);
    void cancelRdv(Long rdvId);
    RdvDto confirmRdv(Long rdvId);
    RdvDto updateStatus(Long id, String status);
}
