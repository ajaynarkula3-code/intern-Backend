package com.smartintern.controller;

import com.smartintern.dto.RecommendationDto;
import com.smartintern.dto.StudentProfileDto;
import com.smartintern.model.Role;
import com.smartintern.security.RequireRole;
import com.smartintern.security.UserContext;
import com.smartintern.service.RecommendationEngineService;
import com.smartintern.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final RecommendationEngineService recommendationService;

    public StudentController(StudentService studentService,
                             RecommendationEngineService recommendationService) {
        this.studentService = studentService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/me")
    @RequireRole(Role.STUDENT)
    public ResponseEntity<?> getMyProfile() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
        return ResponseEntity.ok(studentService.getProfileByUserId(userId));
    }

    @PutMapping("/me")
    @RequireRole(Role.STUDENT)
    public ResponseEntity<?> updateMyProfile(@RequestBody StudentProfileDto dto) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
        return ResponseEntity.ok(studentService.updateProfile(userId, dto));
    }

    @GetMapping("/me/recommendations")
    @RequireRole(Role.STUDENT)
    public ResponseEntity<?> getMyRecommendations() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
        List<RecommendationDto> recommendations = recommendationService.getRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping
    @RequireRole({Role.ADMIN, Role.HR})
    public ResponseEntity<List<StudentProfileDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    @RequireRole({Role.ADMIN, Role.HR})
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(studentService.getStudentById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
