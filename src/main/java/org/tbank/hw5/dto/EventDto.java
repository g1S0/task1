package org.tbank.hw5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventDto {
    private final String title;
    private final String price;
}
