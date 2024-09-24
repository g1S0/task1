package org.tbank.hw5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String slug;
    private String name;
}
