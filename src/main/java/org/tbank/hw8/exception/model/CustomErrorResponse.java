package org.tbank.hw8.exception.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomErrorResponse {
    @Schema(description = "Error message", defaultValue = "Internal Server Error")
    private final String message;
    @Schema(description = "Error code", defaultValue = "503")
    private final int code;
}
