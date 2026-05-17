package com.luis.bc_tecnoin.customer_api.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ValidationErrorDTO {

    private String code;
    private List<FieldErrorDTO> errors;


}
