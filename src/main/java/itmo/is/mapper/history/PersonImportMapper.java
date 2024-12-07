package itmo.is.mapper.history;

import itmo.is.dto.history.PersonImportDto;
import itmo.is.mapper.EntityMapper;
import itmo.is.mapper.security.UserMapper;
import itmo.is.model.history.PersonImport;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PersonImportMapper extends EntityMapper<PersonImportDto, PersonImport> {
}
