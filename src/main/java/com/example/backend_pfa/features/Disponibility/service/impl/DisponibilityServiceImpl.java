package com.example.backend_pfa.features.Disponibility.service.impl;

import com.example.backend_pfa.features.DTO.DisponibilityDto;
import com.example.backend_pfa.features.Disponibility.dao.entities.Disponibility;
import com.example.backend_pfa.features.Disponibility.dao.repository.DisponibilityRepository;
import com.example.backend_pfa.features.Disponibility.service.DisponibilityService;
import com.example.backend_pfa.features.user.dao.entities.User;
import com.example.backend_pfa.features.user.dao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisponibilityServiceImpl implements DisponibilityService {

    @Autowired
    private DisponibilityRepository disponibilityRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public DisponibilityDto createDisponibility(DisponibilityDto dto) {
        User doctor = userRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        LocalTime start = LocalTime.parse(dto.getStartTime());
        LocalTime end = LocalTime.parse(dto.getEndTime());

        if (start.isAfter(end)) {
            throw new RuntimeException("Start time must be before end time.");
        }

        List<Disponibility> existing = disponibilityRepository.findByDoctorIdAndDayOfWeek(dto.getDoctorId(), dto.getDayOfWeek());

        boolean conflict = existing.stream().anyMatch(d ->
                start.isBefore(LocalTime.parse(d.getEndTime())) &&
                        end.isAfter(LocalTime.parse(d.getStartTime()))
        );

        if (conflict) {
            throw new RuntimeException("Availability already exists or overlaps for this time range on the same day.");
        }

        Disponibility disponibility = new Disponibility();
        disponibility.setDayOfWeek(dto.getDayOfWeek());
        disponibility.setStartTime(dto.getStartTime());
        disponibility.setEndTime(dto.getEndTime());
        disponibility.setSlotDurationMinutes(dto.getSlotDurationMinutes());
        disponibility.setDoctor(doctor);

        disponibilityRepository.save(disponibility);

        dto.setId(disponibility.getId());

        // Generate slots
        if (dto.getSlotDurationMinutes() != null && dto.getSlotDurationMinutes() > 0) {
            List<String> slots = generateSlots(start, end, dto.getSlotDurationMinutes());
            dto.setGeneratedSlots(slots);
        }

        return dto;
    }

    private List<String> generateSlots(LocalTime start, LocalTime end, int duration) {
        List<String> slots = new ArrayList<>();
        LocalTime current = start;

        while (current.plusMinutes(duration).compareTo(end) <= 0) {
            LocalTime slotEnd = current.plusMinutes(duration);
            slots.add(current + " - " + slotEnd);
            current = slotEnd;
        }
        return slots;
    }


    @Override
    public List<DisponibilityDto> getDisponibilitiesByDoctor(Long doctorId) {
        return disponibilityRepository.findByDoctorId(doctorId)
                .stream()
                .map(d -> {
                    DisponibilityDto dto = new DisponibilityDto();
                    dto.setId(d.getId());
                    dto.setDayOfWeek(d.getDayOfWeek());
                    dto.setStartTime(d.getStartTime());
                    dto.setEndTime(d.getEndTime());
                    dto.setDoctorId(d.getDoctor().getId());
                    dto.setSlotDurationMinutes(d.getSlotDurationMinutes());

                    // Add this line to generate slots
                    if (d.getSlotDurationMinutes() != null && d.getSlotDurationMinutes() > 0) {
                        List<String> slots = generateSlots(LocalTime.parse(d.getStartTime()), LocalTime.parse(d.getEndTime()), d.getSlotDurationMinutes());
                        dto.setGeneratedSlots(slots);
                    }

                    return dto;
                }).collect(Collectors.toList());
    }


    @Override
    public DisponibilityDto updateDisponibility(Long id, DisponibilityDto dto) {
        Disponibility disponibility = disponibilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibility not found"));

        if (!disponibility.getDoctor().getId().equals(dto.getDoctorId())) {
            throw new RuntimeException("Unauthorized update attempt");
        }

        LocalTime start = LocalTime.parse(dto.getStartTime());
        LocalTime end = LocalTime.parse(dto.getEndTime());

        if (start.isAfter(end)) {
            throw new RuntimeException("Start time must be before end time.");
        }

        List<Disponibility> existing = disponibilityRepository.findByDoctorIdAndDayOfWeek(dto.getDoctorId(), dto.getDayOfWeek());

        boolean conflict = existing.stream().anyMatch(d ->
                !d.getId().equals(id) &&
                        start.isBefore(LocalTime.parse(d.getEndTime())) &&
                        end.isAfter(LocalTime.parse(d.getStartTime()))
        );

        if (conflict) {
            throw new RuntimeException("Updated time overlaps with an existing availability.");
        }

        disponibility.setDayOfWeek(dto.getDayOfWeek());
        disponibility.setStartTime(dto.getStartTime());
        disponibility.setEndTime(dto.getEndTime());
        disponibility.setSlotDurationMinutes(dto.getSlotDurationMinutes());

        disponibilityRepository.save(disponibility);
        dto.setId(disponibility.getId());

        // Update generated slots
        if (dto.getSlotDurationMinutes() != null && dto.getSlotDurationMinutes() > 0) {
            List<String> slots = generateSlots(start, end, dto.getSlotDurationMinutes());
            dto.setGeneratedSlots(slots);
        }

        return dto;
    }

    @Override
    public void deleteDisponibility(Long id) {
        disponibilityRepository.deleteById(id);
    }
}
