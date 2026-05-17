package com.luis.bc_tecnoin.auth_api.service;

import com.luis.bc_tecnoin.auth_api.dto.AuthResponseDto;
import com.luis.bc_tecnoin.auth_api.dto.LoginRequestDto;
import com.luis.bc_tecnoin.auth_api.dto.RegisterRequestDto;
import com.luis.bc_tecnoin.auth_api.dto.TokenValidationResponseDto;

public interface IAuthService {

    AuthResponseDto login(LoginRequestDto dto);
    AuthResponseDto register(RegisterRequestDto dto);
    TokenValidationResponseDto validateToken(String token);
    boolean existsUser(Long userId);

}