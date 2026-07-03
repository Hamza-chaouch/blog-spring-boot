package com.example.blogspringboot.service;

import com.example.blogspringboot.dto.user.LoginRequest;
import com.example.blogspringboot.dto.user.LoginResponse;
import com.example.blogspringboot.dto.user.UserResponse;
import com.example.blogspringboot.entity.User;
import com.example.blogspringboot.exception.ResourceNotFoundException;
import com.example.blogspringboot.repository.UserRepository;
import com.example.blogspringboot.security.CustomUserDetails;
import com.example.blogspringboot.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        // Si on arrive ici, l'authentification a réussi (sinon exception levée automatiquement)

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        String token = jwtService.generateToken(new CustomUserDetails(user));

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .createdAt(user.getCreatedAt())
                .build();

        return new LoginResponse(token, userResponse);
    }
}