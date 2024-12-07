package itmo.is.service.history;

import itmo.is.dto.history.PersonImportDto;
import itmo.is.mapper.history.PersonImportMapper;
import itmo.is.model.history.PersonImport;
import itmo.is.model.security.User;
import itmo.is.repository.PersonImportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PersonHistoryService {
    private final PersonImportRepository personImportRepository;
    private final PersonImportMapper personImportMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PersonImport createStartedImportLog() {
        return personImportRepository.save(new PersonImport());
    }

    @Transactional
    public void saveFinishedImportLog(PersonImport importLog) {
        personImportRepository.save(importLog);
    }

    public Page<PersonImportDto> findAll(Pageable pageable) {
        return personImportRepository.findAll(pageable).map(personImportMapper::toDto);
    }

    public Page<PersonImportDto> findAllByUser(User user, Pageable pageable) {
        return personImportRepository.findAllByUser(user, pageable).map(personImportMapper::toDto);
    }
}
