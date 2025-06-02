package com.example.backend_pfa.features.DTO;

import com.example.backend_pfa.features.user.enums.Role;

import com.example.backend_pfa.features.user.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;


@Embeddable
@Data

public class PatientDto {
    private Long id ;

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

    //for patient

    private String bloodGroup;
    private String type;

}
