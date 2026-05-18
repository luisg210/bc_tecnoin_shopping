package com.luis.bc_tecnoin.payment_api.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Schema(description = "Validation error response")
public class ValidationErrorDTO {

    @Schema(description = "Error code", example = "VALIDATION_ERROR")
    private String code;

    @Schema(description = "List of field-level errors")
    private List<FieldErrorDTO> errors;

}
