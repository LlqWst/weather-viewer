package dev.lqwd.mapper;

import dev.lqwd.dto.weather.AddLocationRequestDTO;
import dev.lqwd.entity.Location;
import dev.lqwd.entity.User;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "user", target = "user")
    @Mapping(source = "addLocationRequestDTO.name", target = "name")
    @Mapping(source = "addLocationRequestDTO.lat", target = "latitude")
    @Mapping(source = "addLocationRequestDTO.lon", target = "longitude")
    Location toLocation(AddLocationRequestDTO addLocationRequestDTO, User user);

}
