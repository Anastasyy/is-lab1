package itmo.is.mapper.domain;

import itmo.is.dto.domain.PersonDto;
import itmo.is.dto.domain.request.CreatePersonRequest;
import itmo.is.dto.domain.request.UpdatePersonRequest;
import itmo.is.mapper.EntityMapper;
import itmo.is.model.domain.Person;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {ColorMapper.class, CoordinatesMapper.class, CountryMapper.class, LocationMapper.class}
)
public interface PersonMapper extends EntityMapper<PersonDto, Person> {
    Person toEntity(CreatePersonRequest createPersonRequest);

    Person toEntity(UpdatePersonRequest updatePersonRequest);
}
