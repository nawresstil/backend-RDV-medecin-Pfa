package com.example.backend_pfa.features.user.service.impl;


import com.example.backend_pfa.features.DTO.DoctorDto;
import com.example.backend_pfa.features.DTO.PatientDto;
import com.example.backend_pfa.features.Speciality.dao.entities.Speciality;
import com.example.backend_pfa.features.user.dao.entities.User;
import com.example.backend_pfa.features.user.dao.repositories.UserRepository;
import com.example.backend_pfa.features.user.enums.Role;
import com.example.backend_pfa.features.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Override
    public ResponseEntity<List<User>> getAllUsers() {

        List<User> user = userRepository.findAll();

        return ResponseEntity.ok().body(user);

    }
    @Override
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(user);
    }

    @Override
    public ResponseEntity<User> updateUser(Long id, User userDetails, MultipartFile profilePicture) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Password change logic
        if (userDetails.getPassword() != null) {
            if (userDetails.getOldPassword() == null || !passwordEncoder.matches(userDetails.getOldPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body(null);
            }

            if (!userDetails.getPassword().equals(userDetails.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(null);
            }

            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        // Update other fields
        user.setUsername(userDetails.getUsername());
        user.setFirstname(userDetails.getFirstname());
        user.setLastname(userDetails.getLastname());
        user.setPhone(userDetails.getPhone());
        user.setRole(userDetails.getRole());

        // ✅ Save profile picture if present
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                String uploadDir = "uploads/profile-pictures/";
                String originalFilename = profilePicture.getOriginalFilename();
                String fileName = System.currentTimeMillis() + "_" + originalFilename;
                String filePath = uploadDir + fileName;

                File dest = new File(filePath);
                dest.getParentFile().mkdirs(); // create dir if not exists
                profilePicture.transferTo(dest);

                user.setProfilePicture(fileName); // Save just the filename (optional: full path)
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<User> forgotPassword(Map<String, String> requestMap) {
        try {
            String email = requestMap.get("email");
            User user = userRepository.findByEmail(email);

            if (user == null) {
                // User not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Generate and save a new password for the user
            String newPassword = generateNewPassword();
            String encodedPassword = passwordEncoder.encode(newPassword); // Encode the password
            user.setPassword(encodedPassword);

            userRepository.save(user);

            // Send the new password to the user (you can customize this part based on your requirements)
            sendPasswordByEmail(user.getEmail(), newPassword);

            return ResponseEntity.ok(user);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Override
    public ResponseEntity<User> getUserByFirstname(@PathVariable String firstname) {
        User user = userRepository.findUserByUsername(firstname);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(user);
    }

    // Method to generate a new password (you can customize this method based on your requirements)
    private String generateNewPassword() {
        // Generate a random password or implement your own logic
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 10;
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * allowedChars.length());
            password.append(allowedChars.charAt(randomIndex));
        }

        return password.toString();
    }
    @Autowired
    private JavaMailSender javaMailSender;
    // Method to send the new password to the user (you can customize this method based on your requirements)
    private void sendPasswordByEmail(String email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nawresstilouch1@gmail.com");
        message.setTo(email);
        message.setSubject("Password Reset");
        message.setText("Your new password is: "+ password);

        javaMailSender.send(message);
    }


    public ResponseEntity<List<User>> getUsersByRole(Role role) {
        List<User> users = userRepository.findAllByRole(role);
        return ResponseEntity.ok(users);
    }


    @Override
    public ResponseEntity<List<User>> getAllDoctors() {
        return ResponseEntity.ok(userRepository.findAllByRole(Role.DOCTOR));
    }

    @Override
    public ResponseEntity<List<User>> getAllPatients() {
        return ResponseEntity.ok(userRepository.findAllByRole(Role.PATIENT));
    }

    // … existing methods …
    @Override
    public DoctorDto updateDoctor(DoctorDto dto, MultipartFile profilePicture, MultipartFile[] clinicImages) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.DOCTOR) {
            throw new AccessDeniedException("Not a doctor");
        }

        // Shared user fields
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setZipCode(dto.getZipCode());
        user.setCountry(dto.getCountry());
        user.setJoiningDate(dto.getJoiningDate());
        user.setUsername(dto.getUsername());
        // Handle password change
        if (dto.getOldPassword() != null && !dto.getOldPassword().isBlank()) {
            if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Ancien mot de passe incorrect");
            }
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
            }
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // Doctor-specific fields
        user.setGender(dto.getGender());
        user.setAboutMe(dto.getAboutMe());
        user.setBiography(dto.getBiography());
        user.setClinicName(dto.getClinicName());
        user.setClinicAddress(dto.getClinicAddress());
        user.setClinicContact(dto.getClinicContact());
        user.setFree(dto.isFree());
        user.setCustomPrice(dto.getCustomPrice());
        // Set education, experience, awards, memberships, registrations
        user.setEducation(dto.getEducation());
        user.setExperience(dto.getExperience());
        user.setAwards(dto.getAwards());
        user.setMemberships(dto.getMemberships());
        user.setRegistrations(dto.getRegistrations());


        // Handle profile picture upload
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                String originalFilename = profilePicture.getOriginalFilename();
                if (originalFilename == null || originalFilename.trim().isEmpty()) {
                    throw new RuntimeException("Nom de fichier invalide");
                }

                String uploadDir = new File("uploads/profile-pictures/").getAbsolutePath();
                String fileName = System.currentTimeMillis() + "_" + originalFilename;
                File dest = new File(uploadDir, fileName);
                dest.getParentFile().mkdirs();
                profilePicture.transferTo(dest);

                user.setProfilePicture(fileName);
            } catch (IOException e) {
                e.printStackTrace(); // pour avoir le détail complet dans la console
                throw new RuntimeException("Erreur lors de l'upload de la photo de profil", e);
            }
        }

        // 🔵 Upload multiple clinic images
        if (clinicImages != null && clinicImages.length > 0) {
            List<String> clinicImageFilenames = new ArrayList<>();

            for (MultipartFile image : clinicImages) {
                if (image != null && !image.isEmpty()) {
                    try {
                        String uploadDir = new File("uploads/clinic-images/").getAbsolutePath();
                        new File(uploadDir).mkdirs();

                        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                        File dest = new File(uploadDir, fileName);
                        image.transferTo(dest);

                        clinicImageFilenames.add(fileName);
                    } catch (IOException e) {
                        throw new RuntimeException("Erreur lors de l'upload des images de clinique", e);
                    }
                }
            }

            // ✅ Save list as JSON string or associate via new entity if normalized
            user.setClinicImages(clinicImageFilenames); // if it's a List<String> in User entity
        }


        User updatedUser = userRepository.save(user);
        return mapToDoctorDto(updatedUser);
    }
    // --- Mappers ---
    public DoctorDto mapToDoctorDto(User user) {
        DoctorDto dto = new DoctorDto();
        BeanUtils.copyProperties(user, dto);
        dto.setServices(user.getServices());
        dto.setSpecialityIds(user.getSpecialities()
                .stream()
                .map(Speciality::getId)
                .collect(Collectors.toList()));
        dto.setEducation(user.getEducation());
        dto.setExperience(user.getExperience());
        dto.setAwards(user.getAwards());
        dto.setMemberships(user.getMemberships());
        dto.setRegistrations(user.getRegistrations());
        return dto;
    }

    @Override
    public PatientDto updatePatient(PatientDto dto, MultipartFile profilePicture) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.PATIENT) {
            throw new AccessDeniedException("Not a patient");
        }

        // Shared fields
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setZipCode(dto.getZipCode());
        user.setCountry(dto.getCountry());
        user.setJoiningDate(dto.getJoiningDate());
        user.setUsername(dto.getUsername());

        // Password change
        if (dto.getOldPassword() != null && !dto.getOldPassword().isBlank()) {
            if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Old password incorrect");
            }
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                throw new IllegalArgumentException("Passwords do not match");
            }
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // Profile picture upload
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                String originalFilename = profilePicture.getOriginalFilename();
                if (originalFilename == null || originalFilename.trim().isEmpty()) {
                    throw new RuntimeException("Invalid filename");
                }

                String uploadDir = new File("uploads/profile-pictures/").getAbsolutePath();
                String fileName = System.currentTimeMillis() + "_" + originalFilename;
                File dest = new File(uploadDir, fileName);
                dest.getParentFile().mkdirs();
                profilePicture.transferTo(dest);

                user.setProfilePicture(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Profile picture upload failed", e);
            }
        }

        // Patient-specific field
        user.setBloodGroup(dto.getBloodGroup());
        User updatedUser = userRepository.save(user);
        return mapToPatientDto(updatedUser);
    }






    private PatientDto mapToPatientDto(User user) {
        PatientDto dto = new PatientDto();
        BeanUtils.copyProperties(user, dto);
        dto.setBloodGroup(user.getBloodGroup());
        return dto;
    }




//    @Override
//    public ResponseEntity<List<DoctorDto>> getDoctors(String keyword) {
//        List<User> doctors = userRepository.findAllByRole(Role.DOCTOR);
//
//        List<DoctorDto> doctorDtos = doctors.stream()
//                .map(user -> {
//                    DoctorDto dto = new DoctorDto();
//                    dto.setId(user.getId());
//                    dto.setUsername(user.getUsername());
//                    dto.setEmail(user.getEmail());
//                    dto.setClinicName(user.getClinicName());
//                    dto.setServices(user.getServices());
//                    dto.setEducation(user.getEducation());
//                    dto.setExperience(user.getExperience());
//                    dto.setAwards(user.getAwards());
//                    dto.setMemberships(user.getMemberships());
//                    dto.setRegistrations(user.getRegistrations());
//                    // Autres champs si nécessaire
//                    return dto;
//                })
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(doctorDtos);
//    }
//    @Override
//    public ResponseEntity<List<DoctorDto>> getDoctors(String keyword) {
//        List<User> doctors;
//        if (keyword != null && !keyword.isEmpty()) {
//            doctors = userRepository.findDoctorsByKeyword(Role.DOCTOR, keyword);
//        } else {
//            doctors = userRepository.findAllByRole(Role.DOCTOR);
//        }
//
//                List<DoctorDto> doctorDtos = doctors.stream()
//                .map(user -> {
//                    DoctorDto dto = new DoctorDto();
//                    dto.setId(user.getId());
//                    dto.setFirstname(user.getFirstname());
//                    dto.setUsername(user.getUsername());
//                    dto.setEmail(user.getEmail());
//                    dto.setClinicName(user.getClinicName());
//                    dto.setServices(user.getServices());
//                    dto.setEducation(user.getEducation());
//                    dto.setExperience(user.getExperience());
//                    dto.setAwards(user.getAwards());
//                    dto.setMemberships(user.getMemberships());
//                    dto.setRegistrations(user.getRegistrations());
//                    // Autres champs si nécessaire
//                    return dto;
//                })
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(doctorDtos);
//    }
@Override
public ResponseEntity<List<DoctorDto>> getDoctors(String keyword) {
    List<User> doctors;
    if (keyword != null && !keyword.isEmpty()) {
        doctors = userRepository.findDoctorsByKeyword(Role.DOCTOR, keyword);
    } else {
        doctors = userRepository.findAllByRole(Role.DOCTOR);
    }

    // Convert List<User> to List<DoctorDto> using ModelMapper
    List<DoctorDto> doctorDtos = doctors.stream()
            .map(user -> modelMapper.map(user, DoctorDto.class))
            .collect(Collectors.toList());

    return ResponseEntity.ok(doctorDtos);
}




}
