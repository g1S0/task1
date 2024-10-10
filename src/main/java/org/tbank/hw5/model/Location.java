package org.tbank.hw5.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    private final String slug;
    private final String name;
}
