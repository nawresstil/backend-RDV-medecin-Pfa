package com.example.backend_pfa.features.Speciality.controllers;

import com.example.backend_pfa.features.Speciality.dao.entities.Speciality;
import com.example.backend_pfa.features.Speciality.service.SpecialityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
    @RequestMapping("/speciality")
    @Slf4j
    public class SpecialityController {
        private final SpecialityService specialityService;

        public SpecialityController(SpecialityService specialityService) {
            this.specialityService = specialityService;
        }

    @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Speciality> createSpeciality(
            @RequestParam("name") String name,
            @RequestParam("image") MultipartFile image) {
        try {
            Speciality speciality = specialityService.createSpeciality(name, image);
            return new ResponseEntity<>(speciality, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<Speciality>> getAllSpeciality() {

        return specialityService.getAllSpeciality();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Speciality> getSpecialityById(@PathVariable Long id) {

        return specialityService.getSpecialityById(id);
    }
    @PutMapping(path = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Speciality> updateSpeciality(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) throws IOException {
        Speciality updated = specialityService.updateSpeciality(id, name, imageFile);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Speciality> deleteSpeciality(@PathVariable Long id) {
        return specialityService.deleteSpeciality(id);
    }
}

