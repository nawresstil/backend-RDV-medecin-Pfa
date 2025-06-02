package com.example.backend_pfa.features.Speciality.service.impl;

import com.example.backend_pfa.features.Speciality.dao.entities.Speciality;
import com.example.backend_pfa.features.Speciality.dao.repository.SpecialityRepository;
import com.example.backend_pfa.features.Speciality.service.SpecialityService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class SpecialityServiceImpl implements SpecialityService {

    private final SpecialityRepository specialityRepository;


    public SpecialityServiceImpl(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }

    @Override
    public Speciality createSpeciality(String name, MultipartFile imageFile) throws IOException {
        String imagePath = saveImage(imageFile);

        Speciality speciality = new Speciality();
        speciality.setName(name);
        speciality.setImageUrl(imagePath);

        return specialityRepository.save(speciality);
    }



    @Override
    public Speciality updateSpeciality(Long id, String name, MultipartFile imageFile) throws IOException {
        Speciality speciality = specialityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Speciality not found with id: " + id));

        // Update the name
        speciality.setName(name);

        // Update the image if a new one is provided
        if (imageFile != null && !imageFile.isEmpty()) {
            // Save the new image using the same method as in create
            String newImagePath = saveImage(imageFile);

            // OPTIONAL: delete old image file if needed
            String oldImagePath = speciality.getImageUrl();
            if (oldImagePath != null && !oldImagePath.isEmpty()) {
                Path oldImage = Paths.get("uploads/").resolve(oldImagePath);
                Files.deleteIfExists(oldImage);
            }

            // Set new image path
            speciality.setImageUrl(newImagePath);
            System.out.println(">>> Received update request for ID: " + id);
            System.out.println(">>> Name: " + name);
            System.out.println(">>> Image file: " + (imageFile != null ? imageFile.getOriginalFilename() : "null"));

        }

        // Save the updated speciality
        return specialityRepository.save(speciality);
    }


    private String saveImage(MultipartFile imageFile) throws IOException {
        String uploadDir = "uploads/";
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }


    @Override
    public ResponseEntity<Speciality> getSpecialityById(@PathVariable Long id) {
        Speciality speciality = specialityRepository.findById(id).orElse(null);

        if (speciality == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(speciality);
    }
    @Override
    public ResponseEntity<Speciality> deleteSpeciality(@PathVariable Long id) {
        Speciality speciality = specialityRepository.findById(id).orElse(null);

        if (speciality == null) {
            return ResponseEntity.notFound().build();
        }
        specialityRepository.delete(speciality);
        return ResponseEntity.ok().build();
    }
    @Override
    public ResponseEntity<List<Speciality>> getAllSpeciality() {

        List<Speciality> speciality = specialityRepository.findAll();
        return ResponseEntity.ok().body(speciality);
    }
}
