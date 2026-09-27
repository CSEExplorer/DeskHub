package com.Peabody.deskhub.auth.service.Impl;

import com.Peabody.deskhub.auth.dto.LoginRequest;
import com.Peabody.deskhub.auth.dto.RegisterRequest;
import com.Peabody.deskhub.auth.entity.User;
import com.Peabody.deskhub.auth.exception.InvalidCredentialsException;
import com.Peabody.deskhub.auth.exception.PasswordTooShortException;
import com.Peabody.deskhub.auth.exception.UserAlreadyExistsException;
import com.Peabody.deskhub.auth.exception.UserNotFoundException;
import com.Peabody.deskhub.auth.repository.UserRepository;
import com.Peabody.deskhub.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;

    public Map<String, Object> register(RegisterRequest request) {

        // 1️⃣ Email check
        if (userRepository.existsByEmployeeId(request.employeeId())) {
            throw new UserAlreadyExistsException("Employee already Exists");
        }
        if(request.password().length()<5){
            throw new PasswordTooShortException("You password is too short , it should be of length greater than 5");
        }
        // 2️⃣ Hash password
        String hashedPassword = passwordEncoder.encode(request.password());




        // 5️⃣ Create new user
        User user = User.builder()
                .employeeId(request.employeeId())
                .email(request.email())
                .password(hashedPassword)
                .active(true)
                .build();

        // 6️⃣ Save user
        userRepository.save(user);
        // 8️⃣ Return response
        return Map.of(
                "message", "User registered successfully",
                "userId", user.getId(),
                "email",user.getEmail()
        );
    }


    public String login(LoginRequest request) {

        User user = userRepository
                .findByEmployeeId(request.employeeId())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with employee id: "
                                        + request.employeeId()
                        )
                );

        boolean isPasswordValid = passwordEncoder.matches(
                request.password(),
                user.getPassword()
        );

        if (!isPasswordValid) {
            throw new InvalidCredentialsException(
                    "Invalid password"
            );
        }

        if (!user.getActive()) {
            throw new InvalidCredentialsException(
                    "User account is inactive"
            );
        }

        return jwtService.generateAccessToken(user);
    }

    private User getEmployeeByEmployeeId(String employeeId) {

        return userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Employee is not registered or Maybe logged Out"
                        )
                );
    }


}
