package itmo.is.service.history;

import itmo.is.dto.history.PersonImportDto;
import itmo.is.mapper.history.PersonImportMapper;
import itmo.is.model.history.PersonImport;
import itmo.is.model.security.User;
import itmo.is.repository.PersonImportRepository;
import itmo.is.service.aws.S3Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PersonHistoryService {
    private final PersonImportRepository personImportRepository;
    private final PersonImportMapper personImportMapper;
    private final S3Service s3Service;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PersonImport createStartedImportLog() {
        return personImportRepository.save(new PersonImport());
    }

    @Transactional
    public PersonImportDto saveFinishedImportLog(PersonImport importLog) {
        return personImportMapper.toDto(personImportRepository.save(importLog));
    }

    public Page<PersonImportDto> findAll(Pageable pageable) {
        return personImportRepository.findAll(pageable).map(personImportMapper::toDto);
    }

    public Page<PersonImportDto> findAllByUser(User user, Pageable pageable) {
        return personImportRepository.findAllByUser(user, pageable).map(personImportMapper::toDto);
    }

    public void saveImportFile(MultipartFile file, PersonImport importLog) {
        String filename = filenameByImportId(importLog.getId());
        s3Service.save(filename, file);
    }

    public ByteArrayResource getFileByImportId(Long importId) {
        PersonImport importLog = personImportRepository.findById(importId)
                .orElseThrow(() -> new EntityNotFoundException("Import not found with id: " + importId));
        if (!importLog.isSuccess()) {
            throw new IllegalArgumentException("Unsuccessful import");
        }
        String filename = filenameByImportId(importId);
        return s3Service.get(filename);
    }

    private String filenameByImportId(Long importId) {
        return String.format("%s/%d", "people", importId);
    }
}
