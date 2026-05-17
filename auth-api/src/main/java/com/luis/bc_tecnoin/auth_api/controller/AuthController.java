package com.luis.bc_tecnoin.auth_api.controller;

import com.luis.bc_tecnoin.auth_api.dto.AuthResponseDto;
import com.luis.bc_tecnoin.auth_api.dto.LoginRequestDto;
import com.luis.bc_tecnoin.auth_api.dto.RegisterRequestDto;
import com.luis.bc_tecnoin.auth_api.dto.TokenValidationResponseDto;
import com.luis.bc_tecnoin.auth_api.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
        return ResponseEntity.ok(authService.register(request));
    }


    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponseDto> validateToken(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenValidationResponseDto(false, "Authorization header missing or invalid"));
        }

        String token = authHeader.substring(7);

        TokenValidationResponseDto response = authService.validateToken(token);

        if (!response.valid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/auth/{id}/exists
     * Returns true if user exists, false otherwise.
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        boolean exists = authService.existsUser(id);
        return ResponseEntity.ok(exists);
    }
}