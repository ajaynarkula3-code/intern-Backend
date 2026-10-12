package com.smartintern.repository;

import com.smartintern.model.Internship;
import com.smartintern.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {
    List<Internship> findByActiveTrue();
    List<Internship> findByPostedBy(User user);
    List<Internship> findByPostedById(Long userId);
    
    @Query("SELECT i FROM Internship i WHERE i.active = true AND " +
           "(LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.domain) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Internship> searchActiveInternships(@Param("keyword") String keyword);

    long countByActiveTrue();
}
