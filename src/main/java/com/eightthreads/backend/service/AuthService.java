package com.eightthreads.backend.service;

import com.eightthreads.backend.dto.request.UserRegisterRequest;

import java.util.Map;

public interface AuthService {
    void register(UserRegisterRequest request);
    void processForgotPassword(String email);
    void resetPassword(String token, String newPassword);
    Map<String, Object> login(String account, String password);
}