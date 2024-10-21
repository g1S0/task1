package org.tbank.hw10.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PlaceDto {
    private Long id;
    private String slug;
    private String name;
    private List<EventDto> events;
}
