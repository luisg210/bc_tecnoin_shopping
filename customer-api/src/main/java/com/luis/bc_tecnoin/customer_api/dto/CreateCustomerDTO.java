package com.luis.bc_tecnoin.customer_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateCustomerDTO {

    @NotBlank(message = "Nombre es obligatoria")
    private String name;
    @NotBlank(message = "Email es obligatorio")
    @Email(message = "Debes de ingresar un email valido")
    private String email;
    private Long userId;
    private String address;
    private String phone;

}