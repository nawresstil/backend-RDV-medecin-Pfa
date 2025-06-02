package com.example.backend_pfa.features.user.dao.entities;

import com.example.backend_pfa.features.DTO.*;
import com.example.backend_pfa.features.Disponibility.dao.entities.Disponibility;
import com.example.backend_pfa.features.Speciality.dao.entities.Speciality;
import com.example.backend_pfa.features.user.enums.Role;
import com.example.backend_pfa.features.user.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    private String firstname;

    private String lastname;

    private Date dateOfBirth;

    @Column(unique = true)
    private String email;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String zipCode;

    private String country;

    private String joiningDate;

    @Column(unique = true)
    private String username;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String confirmPassword;



    private String oldPassword;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "profile_picture")
    private String profilePicture;

    // Doctor-specific fields
    private String gender;
    private String aboutMe;
    private String biography;

    private String clinicName;
    private String clinicAddress;
    private String clinicLogo;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @ElementCollection
    @CollectionTable(name = "clinic_images", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "image_path")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<String> clinicImages;

    private List<Long> specialityIds; // Used for incoming data from frontend

    private String clinicContact;

    private boolean isFree;
    private Double customPrice;

//    @ElementCollection
//    @CollectionTable(
//            name = "doctors_services",
//            joinColumns = @JoinColumn(name = "user_id"),
//            foreignKey = @ForeignKey(name = "fk_user_services", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE")
//    )
    @ElementCollection
    @CollectionTable(name = "user_service", joinColumns = @JoinColumn(name = "user_id"))
    private List<ServicesDto> services;


    @Transient
    private List<String> speciality;

    @ElementCollection
    @CollectionTable(name = "user_education", joinColumns = @JoinColumn(name = "user_id"))
    private List<EducationDto> education;


    @ElementCollection
    @CollectionTable(name = "user_experience", joinColumns = @JoinColumn(name = "user_id"))
    private List<ExperienceDto> experience;

    @ElementCollection
    @CollectionTable(name = "user_awards", joinColumns = @JoinColumn(name = "user_id"))
    private List<AwardDto> awards;

    @Transient
    private List<String> memberships;

    @ElementCollection
    @CollectionTable(name = "user_registrations", joinColumns = @JoinColumn(name = "user_id"))
    private List<RegistrationDto> registrations;


    private String bloodGroup;
    private String type;



    public String getFullName() {
        return firstname + " " + lastname;
    }

//    @JsonIgnore
//    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,mappedBy = "user")
//    private Set<TacheS> tacheS;

    /*@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "action_id")
    private Action action;*/

    @ManyToMany
    @JoinTable(
            name = "doctor_speciality",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "speciality_id")
    )
    @JsonManagedReference
    private List<Speciality> specialities = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }


    @JsonIgnore
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Disponibility> disponibilities;

    public Role getRole() {
        return role;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public CharSequence getOldPassword() {
        return oldPassword;
    }
}
