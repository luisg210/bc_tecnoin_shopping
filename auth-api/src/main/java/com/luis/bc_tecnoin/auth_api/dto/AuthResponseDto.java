package com.luis.bc_tecnoin.auth_api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthResponseDto {

    private Long userId;
    private String email;
    private String token;

}