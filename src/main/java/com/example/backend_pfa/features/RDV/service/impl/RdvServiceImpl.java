package com.example.backend_pfa.features.RDV.service.impl;

import com.example.backend_pfa.features.DTO.RdvDto;
import com.example.backend_pfa.features.RDV.dao.entities.Rdv;
import com.example.backend_pfa.features.RDV.dao.repository.RdvRepository;
import com.example.backend_pfa.features.RDV.enums.RdvStatus;
import com.example.backend_pfa.features.RDV.service.EmailSenderService;
import com.example.backend_pfa.features.RDV.service.RdvService;
import com.example.backend_pfa.features.user.dao.entities.User;
import com.example.backend_pfa.features.user.dao.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RdvServiceImpl implements RdvService {

    private final RdvRepository rdvRepository;
    private final UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;
    @Override
    public RdvDto bookRdv(RdvDto rdvDto) {
        User doctor = userRepository.findById(rdvDto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        User patient = userRepository.findById(rdvDto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // 🔍 Conflict check: same doctor, same date and time overlap, status is PENDING or CONFIRMED
        boolean conflictExists = rdvRepository.hasTimeConflict(
                rdvDto.getDoctorId(),
                rdvDto.getDate(),
                rdvDto.getStartTime(),
                rdvDto.getEndTime(),
                List.of(RdvStatus.PENDING, RdvStatus.CONFIRMED)
        );

        if (conflictExists) {
            throw new RuntimeException("Time slot already booked or pending confirmation for this doctor.");
        }

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
        Rdv savedRdv = rdvRepository.save(rdv);

        try {
            String subject = "Appointment Cancellation Notice";
            String message = String.format("""
                    <html>
                      <body style="font-family: Arial, sans-serif; color: #333;">
                        <div style="border: 1px solid #e0e0e0; border-radius: 8px; padding: 20px; max-width: 600px; margin: auto;">
                          <h2 style="color: #dc3545;">❌ Your Appointment has been Cancelled</h2>
                          <p>Dear %s,</p>
                          <p>We regret to inform you that your appointment with Dr. %s scheduled for:</p>
                          <ul>
                            <li><strong>Date:</strong> %s</li>
                            <li><strong>Time:</strong> %s to %s</li>
                          </ul>
                          <p>has been <strong>cancelled</strong>. Please contact us if you'd like to reschedule.</p>
                          <p style="margin-top: 30px;">Cordialement,</p>
                          <img src="https://i.postimg.cc/JzsRwnLh/logo.png" alt="Platform Logo" style="height: 60px; margin-top: 10px;" />
                        </div>
                      </body>
                    </html>
                    """,
                    savedRdv.getPatient().getFullName(),
                    savedRdv.getDoctor().getFullName(),
                    savedRdv.getDate(),
                    savedRdv.getStartTime(),
                    savedRdv.getEndTime()
            );

            emailSenderService.sendEmail(savedRdv.getPatient().getEmail(), subject, message);
        } catch (Exception e) {
            e.printStackTrace(); // Or use a proper logger
        }
    }


    @Override
    public RdvDto confirmRdv(Long rdvId) {
        Rdv rdv = rdvRepository.findById(rdvId)
                .orElseThrow(() -> new RuntimeException("RDV not found"));
        rdv.setStatus(RdvStatus.CONFIRMED);
        Rdv savedRdv = rdvRepository.save(rdv);

        try {
            String subject = "Your Appointment is Confirmed";
            String message = String.format("""
<html>
  <body style="font-family: Arial, sans-serif; color: #333;">
    <div style="border: 1px solid #e0e0e0; border-radius: 8px; padding: 20px; max-width: 600px; margin: auto;">
      <h2 style="color: #28a745;">✅ Your Appointment is Confirmed</h2>
      <p>Dear %s,</p>
      <p>Your appointment with Dr. %s has been successfully <strong>confirmed</strong> for:</p>
      <ul>
        <li><strong>Date:</strong> %s</li>
        <li><strong>Time:</strong> %s to %s</li>
      </ul>
      <p>We look forward to seeing you.</p>
      <p style="margin-top: 30px;">Cordialement,</p>
      <img src="https://i.postimg.cc/JzsRwnLh/logo.png" alt="Platform Logo" style="height: 60px; margin-top: 10px;" />
    </div>
  </body>
</html>
""",
                    savedRdv.getPatient().getFullName(),
                    savedRdv.getDoctor().getFullName(),
                    savedRdv.getDate(),
                    savedRdv.getStartTime(),
                    savedRdv.getEndTime()
            );

            emailSenderService.sendEmail(savedRdv.getPatient().getEmail(), subject, message);
        } catch (Exception e) {
            e.printStackTrace();  // You may log this instead
        }

        return toDto(savedRdv);
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
