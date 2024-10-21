package itmo.is.mapper.domain;

import itmo.is.dto.domain.ColorDto;
import itmo.is.mapper.EntityMapper;
import itmo.is.model.domain.Color;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ColorMapper extends EntityMapper<ColorDto, Color> {
}
