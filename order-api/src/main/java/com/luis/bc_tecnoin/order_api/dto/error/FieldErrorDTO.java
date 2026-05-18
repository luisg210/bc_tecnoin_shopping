package com.luis.bc_tecnoin.order_api.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Field-level validation error")
public class FieldErrorDTO {

    @Schema(description = "Field name", example = "email")
    private String field;

    @Schema(description = "Error message", example = "Email is required")
    private String message;
}
