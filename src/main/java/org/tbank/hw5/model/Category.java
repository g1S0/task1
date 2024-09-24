package org.tbank.hw5.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {
    private Long id;
    private String slug;
    private String name;
}

