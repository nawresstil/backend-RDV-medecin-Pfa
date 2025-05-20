package com.example.backend_pfa.features.user.service;

import com.example.backend_pfa.features.DTO.DoctorDto;
import com.example.backend_pfa.features.DTO.PatientDto;
import com.example.backend_pfa.features.user.dao.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<List<User>> getAllUsers();
    User getUserByUsername(String username);

    ResponseEntity<User> getUserById(@PathVariable Long id);

    ResponseEntity<User> deleteUser(@PathVariable Long id);

    ResponseEntity<User> forgotPassword(Map<String, String> requestMap);

    ResponseEntity<User> getUserByFirstname(@PathVariable String firstname);

    ResponseEntity<List<User>> getAllDoctors();

    ResponseEntity<List<User>> getAllPatients();

    ResponseEntity<User> updateUser(Long id, User userDetails, MultipartFile profilePicture);

    DoctorDto updateDoctor(DoctorDto doctorDto, MultipartFile profilePicture,MultipartFile[] clinicImages);

    PatientDto updatePatient(PatientDto dto, MultipartFile profilePicture);


    //    DoctorDto updateDoctor(DoctorDto doctorDto);
   // PatientDto updatePatient(PatientDto patientDto);

    // --- Mappers ---
    DoctorDto mapToDoctorDto(User user);

    ResponseEntity<List<DoctorDto>> getDoctors(String keyword);

}