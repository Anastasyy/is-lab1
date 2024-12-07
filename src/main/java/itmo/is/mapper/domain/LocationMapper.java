package itmo.is.mapper.domain;

import itmo.is.dto.domain.LocationDto;
import itmo.is.mapper.EntityMapper;
import itmo.is.model.domain.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper extends EntityMapper<LocationDto, Location> {
}
