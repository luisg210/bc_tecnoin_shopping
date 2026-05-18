package com.luis.bc_tecnoin.payment_api.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldErrorDTO {

    private String field;
    private String message;
}
