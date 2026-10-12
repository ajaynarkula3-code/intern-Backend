package com.smartintern.repository;

import com.smartintern.model.StudentProfile;
import com.smartintern.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    Optional<StudentProfile> findByUser(User user);
    Optional<StudentProfile> findByUserId(Long userId);
    Optional<StudentProfile> findByUserEmail(String email);
}
