package org.tbank.hw5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDto {
    private final Long id;
    private final String slug;
    private final String name;
}
