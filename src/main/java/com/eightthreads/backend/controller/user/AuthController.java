package com.eightthreads.backend.controller.user;

import com.eightthreads.backend.dto.request.user.UserRegisterRequest;
import com.eightthreads.backend.service.user.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.ok(Map.of("message", "Đăng ký tài khoản thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            authService.processForgotPassword(email);
            return ResponseEntity.ok(Map.of("message", "Đã gửi link khôi phục vào email của bạn!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            authService.resetPassword(token, newPassword);
            return ResponseEntity.ok(Map.of("message", "Đổi mật khẩu thành công! Bạn có thể đăng nhập lại."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> body) {
        try {
            String account = body.get("account") == null ? null : body.get("account").toString();
            String password = body.get("password") == null ? null : body.get("password").toString();
            Object adminFlag = body.get("isAdmin");
            boolean isAdminLogin = adminFlag instanceof Boolean
                    ? (Boolean) adminFlag
                    : Boolean.parseBoolean(adminFlag == null ? "false" : adminFlag.toString());
            Map<String, Object> resp = authService.login(account, password, isAdminLogin);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }
}