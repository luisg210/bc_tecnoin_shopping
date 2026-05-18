package com.luis.bc_tecnoin.auth_api.controller;

import com.luis.bc_tecnoin.auth_api.dto.AuthResponseDto;
import com.luis.bc_tecnoin.auth_api.dto.LoginRequestDto;
import com.luis.bc_tecnoin.auth_api.dto.RegisterRequestDto;
import com.luis.bc_tecnoin.auth_api.dto.TokenValidationResponseDto;
import com.luis.bc_tecnoin.auth_api.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Registers a new user and returns a JWT token")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate token", description = "Validates a JWT token from the Authorization header")
    @ApiResponse(responseCode = "200", description = "Token is valid")
    @ApiResponse(responseCode = "401", description = "Token is invalid or missing")
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

    @GetMapping("/{id}/exists")
    @Operation(summary = "Check user exists", description = "Returns whether a user with the given ID exists")
    @ApiResponse(responseCode = "200", description = "Exists check performed successfully")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        boolean exists = authService.existsUser(id);
        return ResponseEntity.ok(exists);
    }
}
