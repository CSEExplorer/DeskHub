package com.Peabody.deskhub.auth.service;


import com.Peabody.deskhub.auth.entity.User;
import io.jsonwebtoken.Claims;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String generateAccessToken(User user);

    String createAccessToken(
            Map<String, Object> claims,
            User user
    );
}
