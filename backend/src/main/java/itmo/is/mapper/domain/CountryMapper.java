package itmo.is.mapper.domain;

import itmo.is.dto.domain.CountryDto;
import itmo.is.mapper.EntityMapper;
import itmo.is.model.domain.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper extends EntityMapper<CountryDto, Country> {
}
