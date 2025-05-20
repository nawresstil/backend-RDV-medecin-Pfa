package com.example.backend_pfa.auth.dao;

import com.example.backend_pfa.features.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String joiningDate;
    private Role role;
//    private String Specialites;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String phone;
}
