package org.tbank.hw10.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PlaceDto {
    private Long id;

    @NotNull(message = "Slug cannot be empty")
    @Size(min = 1, message = "Slug must be at least 1 character long")
    private String slug;

    @NotNull(message = "Name cannot be empty")
    @Size(min = 1, message = "Name must be at least 1 character long")
    private String name;

    private List<EventDto> events;
}
