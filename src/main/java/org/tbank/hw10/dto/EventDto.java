package org.tbank.hw10.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class EventDto {
    private Long id;
    private String name;
    private LocalDate date;
    private Long placeId;
}