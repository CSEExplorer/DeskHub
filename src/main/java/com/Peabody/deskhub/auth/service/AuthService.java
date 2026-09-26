package com.Peabody.deskhub.auth.service;



import com.Peabody.deskhub.auth.dto.LoginRequest;
import com.Peabody.deskhub.auth.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    String login(LoginRequest request);
}