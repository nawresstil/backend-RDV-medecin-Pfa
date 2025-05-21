package com.example.backend_pfa.features.DTO;

import com.example.backend_pfa.features.user.enums.Role;

import jakarta.persistence.ElementCollection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
public class DoctorDto {
    private Long id;

    private String firstname;

    private String lastname;

    private Date dateOfBirth;

    private String email;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String zipCode;

    private String country;

    private String joiningDate;

    private String username;

    private String password;

    private String confirmPassword;



    private String oldPassword;

    private Role role;

    private String profilePicture;

    // Doctor-specific

    private String aboutMe;
    private String biography;
    private String gender;
    private String clinicName;
    private String clinicAddress;
    private String clinicLogo;
    @ElementCollection
    private List<String> clinicImages;
    private String clinicContact;

    private boolean isFree;
    private Double customPrice;
    @ElementCollection
    private List<String> services;
    private List<Long> specialityIds; // Used for incoming data from frontend

    private List<EducationDto> education;
    private List<ExperienceDto> experience;
    private List<AwardDto> awards;
    private List<String> memberships;
    private List<RegistrationDto> registrations;


}

