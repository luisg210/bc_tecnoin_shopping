package com.luis.bc_tecnoin.customer_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCustomerDTO {

    @NotBlank(message = "Nombre es obligatoria")
    private String name;
    @NotBlank(message = "Email es obligatorio")
    @Email(message = "Debes de ingresar un email valido")
    private String email;
    private String address;
    private String phone;

}
