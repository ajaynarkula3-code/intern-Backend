package com.smartintern.service;

import com.smartintern.dto.AuthRequest;
import com.smartintern.dto.AuthResponse;
import com.smartintern.dto.RegisterRequest;
import com.smartintern.model.Role;
import com.smartintern.model.StudentProfile;
import com.smartintern.model.User;
import com.smartintern.repository.StudentProfileRepository;
import com.smartintern.repository.UserRepository;
import com.smartintern.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       StudentProfileRepository studentProfileRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().toLowerCase().trim())) {
            throw new IllegalArgumentException("An account with this email already exists: " + request.getEmail());
        }

        Role role = request.getRole() != null ? request.getRole() : Role.STUDENT;
        if (role == Role.ADMIN) {
            throw new IllegalArgumentException("Admin accounts cannot be self-registered.");
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        User savedUser = userRepository.save(user);

        // If registering as a student, create an empty StudentProfile right away
        if (role == Role.STUDENT) {
            StudentProfile profile = new StudentProfile(savedUser);
            studentProfileRepository.save(profile);
        }

        String token = jwtUtil.generateToken(savedUser.getId(), savedUser.getEmail(), savedUser.getRole().name());

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                "Registration successful"
        );
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        return new AuthResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                "Login successful"
        );
    }
}
