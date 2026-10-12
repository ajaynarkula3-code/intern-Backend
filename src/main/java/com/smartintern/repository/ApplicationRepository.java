package com.smartintern.repository;

import com.smartintern.model.Application;
import com.smartintern.model.ApplicationStatus;
import com.smartintern.model.Internship;
import com.smartintern.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudent(User student);
    List<Application> findByStudentId(Long studentId);
    List<Application> findByInternship(Internship internship);
    List<Application> findByInternshipId(Long internshipId);
    List<Application> findByInternshipPostedById(Long hrId);

    Optional<Application> findByStudentIdAndInternshipId(Long studentId, Long internshipId);
    boolean existsByStudentIdAndInternshipId(Long studentId, Long internshipId);

    long countByStatus(ApplicationStatus status);

    void deleteByInternshipId(Long internshipId);
}
