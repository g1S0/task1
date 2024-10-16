package org.tbank.hw5.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.tbank.hw5.dto.LocationDto;
import org.tbank.hw5.model.Location;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LocationMapperTest {

    private static final String LOCATION_SLUG_1 = "msk";
    private static final String LOCATION_SLUG_2 = "spb";
    private static final String LOCATION_NAME_1 = "Москва";
    private static final String LOCATION_NAME_2 = "Санкт-Петербург";

    private final LocationMapper locationMapper = Mappers.getMapper(LocationMapper.class);

    private Location createLocation(String slug, String name) {
        return new Location(slug, name);
    }

    private LocationDto createLocationDto(String slug, String name) {
        return new LocationDto(slug, name);
    }

    @Test
    public void testToLocationDto() {
        Location location = createLocation(LOCATION_SLUG_1, LOCATION_NAME_1);

        LocationDto locationDto = locationMapper.toLocationDto(location);

        assertNotNull(locationDto);
        assertEquals(LOCATION_SLUG_1, locationDto.getSlug());
        assertEquals(LOCATION_NAME_1, locationDto.getName());
    }

    @Test
    public void testToLocation() {
        LocationDto locationDto = createLocationDto(LOCATION_SLUG_1, LOCATION_NAME_1);

        Location location = locationMapper.toLocation(locationDto);

        assertNotNull(location);
        assertEquals(LOCATION_SLUG_1, location.getSlug());
        assertEquals(LOCATION_NAME_1, location.getName());
    }

    @Test
    public void testToLocationDtoList() {
        List<Location> locations = List.of(
                createLocation(LOCATION_SLUG_1, LOCATION_NAME_1),
                createLocation(LOCATION_SLUG_2, LOCATION_NAME_2)
        );

        List<LocationDto> locationDtoList = locationMapper.toLocationDtoList(locations);

        assertNotNull(locationDtoList);
        assertEquals(2, locationDtoList.size());
        assertEquals(LOCATION_NAME_1, locationDtoList.get(0).getName());
        assertEquals(LOCATION_NAME_2, locationDtoList.get(1).getName());
    }

    @Test
    public void testToLocationList() {
        List<LocationDto> locationDtoList = List.of(
                createLocationDto(LOCATION_SLUG_1, LOCATION_NAME_1),
                createLocationDto(LOCATION_SLUG_2, LOCATION_NAME_2)
        );

        List<Location> locationList = locationMapper.toLocationList(locationDtoList);

        assertNotNull(locationList);
        assertEquals(2, locationList.size());
        assertEquals(LOCATION_NAME_1, locationList.get(0).getName());
        assertEquals(LOCATION_NAME_2, locationList.get(1).getName());
    }
}