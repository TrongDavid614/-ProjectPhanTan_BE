package com.eightthreads.backend.service.impl;

import com.eightthreads.backend.dto.request.UserRegisterRequest;
import com.eightthreads.backend.entity.User;
import com.eightthreads.backend.repository.UserRepository;
import com.eightthreads.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(UserRegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        User newUser = User.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .status("active")
                .authProvider("local")
                .build();

        // 3. Lưu vào DB
        userRepository.save(newUser);
    }
}