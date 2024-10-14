package org.tbank.hw5.mapper;

import org.mapstruct.Mapper;
import org.tbank.hw5.dto.LocationDto;
import org.tbank.hw5.model.Location;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationDto toLocationDto(Location location);

    Location toLocation(LocationDto locationDto);

    List<LocationDto> toLocationDtoList(List<Location> locations);

    List<Location> toLocationList(List<LocationDto> locationDtoList);
}
