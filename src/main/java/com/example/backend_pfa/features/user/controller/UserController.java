package com.example.backend_pfa.features.user.controller;

import com.example.backend_pfa.features.DTO.DoctorDto;
import com.example.backend_pfa.features.DTO.PatientDto;
import com.example.backend_pfa.features.user.dao.entities.User;
import com.example.backend_pfa.features.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {

        return userService.getAllUsers();
    }
    @GetMapping("/profile")
    public User getUser(Principal principal) {
        String username = principal.getName();
        return userService.getUserByUsername(username);
    }
    @GetMapping("/first/{firstname}")
    public ResponseEntity<User> getUserByFirstname(@PathVariable String firstname)
    {
        return userService.getUserByFirstname(firstname);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {

        return userService.getUserById(id);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestPart("userDetails") User userDetails,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture
    ) {
        return userService.updateUser(id, userDetails, profilePicture);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {

        return userService.deleteUser(id);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<User> forgotPassword(@RequestBody Map<String, String> requestMap) {
        return userService.forgotPassword(requestMap);
    }


   // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/doctors")
    public ResponseEntity<List<User>> getAllDoctors() {
        return userService.getAllDoctors();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/patients")
    public ResponseEntity<List<User>> getAllPatients() {
        return userService.getAllPatients();
    }


//    @PreAuthorize("hasRole('DOCTOR')")A
//    @GetMapping("/my-patients")
//    public ResponseEntity<List<User>> getMyPatients(Principal principal) {
//        String doctorUsername = principal.getName();
//        return userService.getPatientsOfDoctor(doctorUsername);
//    }


    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping(path = "/doctor/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateDoctorProfile(
            @RequestParam("doctorDto") String doctorDtoJson,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestParam(value = "clinicImages", required = false) MultipartFile[] clinicImages

    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            DoctorDto doctorDto = objectMapper.readValue(doctorDtoJson, DoctorDto.class);

            DoctorDto updated = userService.updateDoctor(doctorDto, profilePicture, clinicImages);
            return ResponseEntity.ok(updated);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur de parsing JSON : " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du traitement du fichier : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur : " + e.getMessage());
        }
    }



//    @PreAuthorize("hasRole('DOCTOR')")
//    @PutMapping("/doctor/profile")
//    public ResponseEntity<DoctorDto> updateDoctorProfile(
//            @RequestBody DoctorDto doctorDto
//    ) {
//        DoctorDto updated = userService.updateDoctor(doctorDto);
//        return ResponseEntity.ok(updated);
//    }


    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping(path = "/patient/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePatientProfile(
            @RequestParam("patientDto") String patientDtoJson,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PatientDto patientDto = objectMapper.readValue(patientDtoJson, PatientDto.class);

            PatientDto updated = userService.updatePatient(patientDto, profilePicture);
            return ResponseEntity.ok(updated);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur JSON : " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur fichier : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur : " + e.getMessage());
        }
    }



//    @PreAuthorize("hasRole('PATIENT')")
//    @PutMapping("/patient/profile")
//    public ResponseEntity<PatientDto> updatePatientProfile(
//            @RequestBody PatientDto patientDto
//    ) {
//        PatientDto updated = userService.updatePatient(patientDto);
//        return ResponseEntity.ok(updated);
//    }

    @GetMapping("/search_doctors")
    public ResponseEntity<List<DoctorDto>> getDoctors(@RequestParam(required = false) String keyword) {
        return userService.getDoctors(keyword);
    }



}
