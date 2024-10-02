package org.tbank.hw5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationDto {
    private final String slug;
    private final String name;
}
