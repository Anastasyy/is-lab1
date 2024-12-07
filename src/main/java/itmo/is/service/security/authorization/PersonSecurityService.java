package itmo.is.service.security.authorization;

import itmo.is.model.domain.Person;
import itmo.is.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonSecurityService extends OwnedEntitySecurityService<Person, Integer> {
    private final PersonRepository personRepository;

    @Override
    protected Person findById(Integer id) {
        return personRepository.findById(id).orElseThrow();
    }
}
