package itmo.is.service.domain;

import itmo.is.dto.domain.ColorDto;
import itmo.is.dto.domain.CountryDto;
import itmo.is.dto.domain.PersonDto;
import itmo.is.dto.domain.request.CreatePersonRequest;
import itmo.is.dto.domain.request.UpdatePersonRequest;
import itmo.is.dto.domain.response.CountResponse;
import itmo.is.dto.domain.response.PercentageResponse;
import itmo.is.mapper.domain.ColorMapper;
import itmo.is.mapper.domain.CountryMapper;
import itmo.is.mapper.domain.PersonMapper;
import itmo.is.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final CountryMapper countryMapper;
    private final ColorMapper colorMapper;

    public Page<PersonDto> findAllPeople(String name, Pageable pageable) {
        if (name != null && !name.isEmpty()) {
            return personRepository.findAllByNameContaining(name, pageable).map(personMapper::toDto);
        }
        return personRepository.findAll(pageable).map(personMapper::toDto);
    }

    public PersonDto findPersonById(int id) {
        return personRepository.findById(id).map(personMapper::toDto).orElseThrow();
    }

    public PersonDto createPerson(CreatePersonRequest request) {
        return personMapper.toDto(personRepository.save(personMapper.toEntity(request)));
    }

    public PersonDto updatePerson(UpdatePersonRequest request) {
        return personMapper.toDto(personRepository.save(personMapper.toEntity(request)));
    }

    public void deletePerson(int id) {
        personRepository.deleteById(id);
    }

    public CountResponse countPeopleByExactWeight(Integer weight) {
        return new CountResponse(personRepository.countByWeightEquals(weight));
    }

    public CountResponse countPeopleByWeightLessThan(Integer weight) {
        return new CountResponse(personRepository.countByWeightLessThan(weight));
    }

    public List<CountryDto> findDistinctNationalities() {
        return personRepository.findDistinctNationalities().stream().map(countryMapper::toDto).toList();
    }

    public CountResponse countPeopleByHairColor(ColorDto color) {
        return new CountResponse(personRepository.countByHairColorEquals(colorMapper.toEntity(color)));
    }

    public PercentageResponse calculatePercentageOfPeopleByEyeColor(ColorDto color) {
        long total = personRepository.count();

        if (total == 0) {
            return new PercentageResponse(0);
        }

        long countByEyeColor = personRepository.countByEyeColorEquals(colorMapper.toEntity(color));
        double percentage = (double) countByEyeColor / (double) total * 100;

        return new PercentageResponse(percentage);
    }
}
