package org.tbank.hw10.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class EventDto {
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 1, message = "Name must be at least 1 character long")
    private String name;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @NotNull(message = "Place ID cannot be null")
    private Long placeId;
}