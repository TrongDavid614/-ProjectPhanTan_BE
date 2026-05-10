package com.eightthreads.backend.service.impl.user;

import com.eightthreads.backend.common.enums.Role;
import com.eightthreads.backend.dto.request.user.UserRegisterRequest;
import com.eightthreads.backend.entity.User;
import com.eightthreads.backend.repository.UserRepository;
import com.eightthreads.backend.security.JwtTokenProvider;
import com.eightthreads.backend.service.user.AuthService;
import com.eightthreads.backend.service.user.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final Environment environment;

    @Override
    public void register(UserRegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        // Determine whether admin creation is permitted (only in 'dev' profile)
        boolean isDev = false;
        try {
            String[] profiles = environment.getActiveProfiles();
            for (String p : profiles) {
                if ("dev".equalsIgnoreCase(p)) {
                    isDev = true;
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        Role resolvedRole = Role.USER;
        // allow admin role only when explicitly requested AND running in dev profile
        if (Boolean.TRUE.equals(request.getIsAdmin()) && isDev) {
            resolvedRole = Role.ADMIN;
        } else if (request.getRole() != null && "ADMIN".equalsIgnoreCase(request.getRole()) && isDev) {
            resolvedRole = Role.ADMIN;
        }

        User newUser = User.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(resolvedRole)
                .status("active")
                .authProvider("local")
                .build();

        userRepository.save(newUser);
    }
    public void processForgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email này!"));

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        userRepository.save(user);

        String resetLink = "http://localhost:3000/reset-password?token=" + token;

        emailService.sendPasswordResetEmail(email, resetLink);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("Mã khôi phục không hợp lệ hoặc đã hết hạn!"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    @Override
    public Map<String, Object> login(String account, String password) {
        return login(account, password, false);
    }

    @Override
    public Map<String, Object> login(String account, String password, boolean isAdminLogin) {
        User user = userRepository.findByEmail(account)
                .orElseThrow(() -> new RuntimeException("Sai tài khoản hoặc mật khẩu"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Sai tài khoản hoặc mật khẩu");
        }

        // Block admin accounts from logging in via user login page
        if (!isAdminLogin && user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Sai tài khoản hoặc mật khẩu");
        }

        if (isAdminLogin && user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Bạn không có quyền truy cập trang quản trị");
        }

        String token = jwtTokenProvider.generateToken(user);

        Map<String, Object> userInfo = Map.of(
                "id", user.getUserId(),
                "email", user.getEmail(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "role", user.getRole() == null ? null : user.getRole().name()
        );

        return Map.of("token", token, "user", userInfo);
    }
}