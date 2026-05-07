package com.eightthreads.backend.service;

import com.eightthreads.backend.dto.request.UserRegisterRequest;

public interface AuthService {
    void register(UserRegisterRequest request);
}