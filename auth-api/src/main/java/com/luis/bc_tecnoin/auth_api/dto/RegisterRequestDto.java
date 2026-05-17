package com.luis.bc_tecnoin.auth_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegisterRequestDto {

    @NotBlank(message = "Email es obligatorio")
    @Email(message = "Debes de ingresar un email valido")
    private String email;
    @NotBlank(message = "Contraseña es obligatoria")
    private String password;

}