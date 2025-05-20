package com.example.backend_pfa.features.user.dao.repositories;

import com.example.backend_pfa.features.user.dao.entities.User;
import com.example.backend_pfa.features.user.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findUserByUsername(String name);
    List<User> findAllByRole(Role role);

    @Query("SELECT u FROM User u WHERE u.role = :role AND " +
            "(LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.firstname) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.country) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.city) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.clinicName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<User> findDoctorsByKeyword(@Param("role") Role role, @Param("keyword") String keyword);

}
