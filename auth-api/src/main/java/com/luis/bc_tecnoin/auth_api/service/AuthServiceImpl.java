package com.luis.bc_tecnoin.auth_api.service;

import com.luis.bc_tecnoin.auth_api.dto.AuthResponseDto;
import com.luis.bc_tecnoin.auth_api.dto.LoginRequestDto;
import com.luis.bc_tecnoin.auth_api.dto.RegisterRequestDto;
import com.luis.bc_tecnoin.auth_api.dto.TokenValidationResponseDto;
import com.luis.bc_tecnoin.auth_api.entity.UserAccount;
import com.luis.bc_tecnoin.auth_api.exception.InvalidCredentialsException;
import com.luis.bc_tecnoin.auth_api.exception.UserNotFoundException;
import com.luis.bc_tecnoin.auth_api.mapper.UserAccountMapper;
import com.luis.bc_tecnoin.auth_api.repository.UserAccountRepository;
import com.luis.bc_tecnoin.auth_api.security.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountMapper mapper;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDto login(LoginRequestDto dto) {
        log.info("Login attempt for email: {}", dto.getEmail());
        UserAccount userAccount = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found: {}", dto.getEmail());
                    return new UserNotFoundException("Invalid email or password");
                });

        if (!passwordEncoder.matches(dto.getPassword(), userAccount.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(userAccount.getEmail(), userAccount.getUserId(), userAccount.getRole());
        AuthResponseDto response = mapper.toResponseDto(userAccount);
        response.setToken(token);

        return response;
    }

    @Override
    @Transactional
    public AuthResponseDto register(RegisterRequestDto dto) {
        log.info("Registering new user: {}", dto.getEmail());

        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            log.warn("Duplicate registration attempt: {}", dto.getEmail());
            throw new InvalidCredentialsException("Email already in use");
        }

        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(dto.getEmail());
        userAccount.setPassword(passwordEncoder.encode(dto.getPassword()));

        UserAccount saved = repository.save(userAccount);

        log.debug("User registered with ID: {}", saved.getUserId());
        return mapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TokenValidationResponseDto validateToken(String token) {
        try {
            log.info("Valid token for subject: {}", jwtUtil.extractSubject(token));
            boolean valid = jwtUtil.isTokenValid(token);
            return new TokenValidationResponseDto(valid, "Valid token");
        } catch (JwtException e) {
            log.warn("Invalid token: {}", e.getMessage());
            return new TokenValidationResponseDto(false, "Invalid or expired token");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsUser(Long userId) {
        return repository.existsById(userId);
    }
}