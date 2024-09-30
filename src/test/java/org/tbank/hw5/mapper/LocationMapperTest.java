package org.tbank.hw5.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.tbank.hw5.dto.LocationDto;
import org.tbank.hw5.model.Location;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LocationMapperTest {

    private final LocationMapper locationMapper = Mappers.getMapper(LocationMapper.class);

    @Test
    public void testToLocationDto() {
        Location location = new Location("msk", "Москва");

        LocationDto locationDto = locationMapper.toLocationDto(location);

        assertNotNull(locationDto);
        assertEquals("msk", locationDto.getSlug());
        assertEquals("Москва", locationDto.getName());
    }

    @Test
    public void testToLocation() {
        LocationDto locationDto = new LocationDto("msk", "Москва");

        Location location = locationMapper.toLocation(locationDto);

        assertNotNull(location);
        assertEquals("msk", location.getSlug());
        assertEquals("Москва", location.getName());
    }

    @Test
    public void testToLocationDtoList() {
        List<Location> locations = List.of(
                new Location("msk", "Москва"),
                new Location("spb", "Санкт-Петербург")
        );

        List<LocationDto> locationDtoList = locationMapper.toLocationDtoList(locations);

        assertNotNull(locationDtoList);
        assertEquals(2, locationDtoList.size());
        assertEquals("Москва", locationDtoList.get(0).getName());
        assertEquals("Санкт-Петербург", locationDtoList.get(1).getName());
    }

    @Test
    public void testToLocationList() {
        List<LocationDto> locationDtoList = List.of(
                new LocationDto("msk", "Москва"),
                new LocationDto("spb", "Санкт-Петербург")
        );

        List<Location> locationList = locationMapper.toLocationList(locationDtoList);

        assertNotNull(locationList);
        assertEquals(2, locationList.size());
        assertEquals("Москва", locationList.get(0).getName());
        assertEquals("Санкт-Петербург", locationList.get(1).getName());
    }
}