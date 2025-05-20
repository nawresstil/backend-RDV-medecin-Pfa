package com.example.backend_pfa.auth.dao;

import com.example.backend_pfa.features.user.enums.Role;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;
    private Role role;

    public AuthenticationResponse(String token, Role role) {
        this.token = token;
        this.role = role;
    }
}