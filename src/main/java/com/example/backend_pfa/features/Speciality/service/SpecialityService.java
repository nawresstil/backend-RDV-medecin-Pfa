package com.example.backend_pfa.features.Speciality.service;

import com.example.backend_pfa.features.Speciality.dao.entities.Speciality;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

public interface SpecialityService {



    ResponseEntity<Speciality> getSpecialityById(@PathVariable Long id);

    ResponseEntity<Speciality> deleteSpeciality(@PathVariable Long id);

    ResponseEntity<List<Speciality>> getAllSpeciality();

    Speciality createSpeciality(String name, MultipartFile imageFile) throws IOException;

    Speciality updateSpeciality(Long id, String name, MultipartFile image) throws IOException;
}
