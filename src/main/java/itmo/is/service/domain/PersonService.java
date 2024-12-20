package itmo.is.service.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.is.dto.domain.PersonDto;
import itmo.is.dto.domain.request.CreatePersonRequest;
import itmo.is.dto.domain.request.UpdatePersonRequest;
import itmo.is.dto.domain.response.CountResponse;
import itmo.is.dto.domain.response.PercentageResponse;
import itmo.is.dto.history.PersonImportDto;
import itmo.is.exception.UniqueConstraintViolationException;
import itmo.is.mapper.domain.PersonMapper;
import itmo.is.model.domain.Color;
import itmo.is.model.domain.Country;
import itmo.is.model.domain.Person;
import itmo.is.model.history.PersonImport;
import itmo.is.repository.PersonRepository;
import itmo.is.service.history.PersonHistoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final PersonHistoryService personHistoryService;
    private final ObjectMapper objectMapper;

    public Page<PersonDto> findAllPeople(String name, Pageable pageable) {
        if (name != null && !name.isEmpty()) {
            return personRepository.findAllByNameContaining(name, pageable).map(personMapper::toDto);
        }
        return personRepository.findAll(pageable).map(personMapper::toDto);
    }

    public PersonDto findPersonById(int id) {
        return personRepository.findById(id).map(personMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + id));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public PersonImportDto importFile(MultipartFile file) {
        PersonImport personImport = personHistoryService.createStartedImportLog();
        List<CreatePersonRequest> requests = parseFile(file);
        importPeople(requests);
        personImport.setSuccess(true);
        personImport.setObjectsAdded(requests.size());
        personHistoryService.saveImportFile(file, personImport.getId());
        return personHistoryService.saveFinishedImportLog(personImport);
    }

    private List<CreatePersonRequest> parseFile(MultipartFile file) {
        try {
            return objectMapper.readValue(file.getBytes(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON file format", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file", e);
        }
    }

    private void importPeople(List<CreatePersonRequest> requests) {
        List<Person> people = requests.stream().map(personMapper::toEntity).toList();
        validateUniquePersonNameConstraint(people);
        personRepository.saveAll(people);
    }

    public ByteArrayResource getImportFileByImportId(Long importId) {
        return personHistoryService.getFileByImportId(importId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public PersonDto createPerson(CreatePersonRequest request) {
        Person person = personMapper.toEntity(request);
        validateUniquePersonNameConstraint(person);
        return personMapper.toDto(personRepository.save(person));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public PersonDto updatePerson(int id, UpdatePersonRequest request) {
        Person original = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + id));
        Person updated = personMapper.toEntity(request);
        if (!original.getName().equals(updated.getName())) {
            validateUniquePersonNameConstraint(updated);
        }
        updated.setId(id);
        updated.setOwner(original.getOwner());
        updated.setAdminEditAllowed(original.isAdminEditAllowed());
        return personMapper.toDto(personRepository.save(updated));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deletePerson(int id) {
        personRepository.deleteById(id);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void allowAdminEditing(int id) {
        var person = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + id));
        person.setAdminEditAllowed(true);
        personRepository.save(person);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void denyAdminEditing(int id) {
        var person = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + id));
        person.setAdminEditAllowed(false);
        personRepository.save(person);
    }

    public CountResponse countPeopleByExactWeight(Integer weight) {
        return new CountResponse(personRepository.countByWeightEquals(weight));
    }

    public CountResponse countPeopleByWeightLessThan(Integer weight) {
        return new CountResponse(personRepository.countByWeightLessThan(weight));
    }

    public List<Country> findDistinctNationalities() {
        return personRepository.findDistinctNationalities();
    }

    public CountResponse countPeopleByHairColor(Color color) {
        return new CountResponse(personRepository.countByHairColorEquals(color));
    }

    public PercentageResponse calculatePercentageOfPeopleByEyeColor(Color color) {
        long total = personRepository.count();

        if (total == 0) {
            return new PercentageResponse(0);
        }

        long countByEyeColor = personRepository.countByEyeColorEquals(color);
        double percentage = (double) countByEyeColor / (double) total * 100;

        return new PercentageResponse(percentage);
    }

    private void validateUniquePersonNameConstraint(Person person) {
        if (personRepository.existsByName(person.getName())) {
            throw new UniqueConstraintViolationException("Name '" + person.getName() + "' already exists");
        }
    }

    private void validateUniquePersonNameConstraint(List<Person> people) {
        Set<String> names = new HashSet<>();
        people.forEach(person -> {
            if (!names.add(person.getName())) {
                throw new UniqueConstraintViolationException("Name '" + person.getName() + "' already exists");
            }
        });
        personRepository.findFirstByNameIn(names).ifPresent((person) -> {
            throw new UniqueConstraintViolationException("Name '" + person.getName() + "' already exists");
        });
    }
}
