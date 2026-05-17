package com.luis.bc_tecnoin.auth_api.mapper;

import com.luis.bc_tecnoin.auth_api.dto.AuthResponseDto;
import com.luis.bc_tecnoin.auth_api.dto.LoginRequestDto;
import com.luis.bc_tecnoin.auth_api.dto.RegisterRequestDto;
import com.luis.bc_tecnoin.auth_api.entity.UserAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {

    AuthResponseDto toResponseDto(UserAccount entity);
    UserAccount toEntityLogin(LoginRequestDto dto);
    UserAccount toEntity(RegisterRequestDto dto);


}
