package itmo.is.repository;

import itmo.is.model.history.PersonImport;
import itmo.is.model.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonImportRepository extends JpaRepository<PersonImport, Long> {
    Page<PersonImport> findAllByUser(User user, Pageable pageable);
}
