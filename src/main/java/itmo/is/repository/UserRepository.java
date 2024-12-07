package itmo.is.repository;

import itmo.is.model.security.Role;
import itmo.is.model.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(@NonNull String username);

    boolean existsByUsername(@NonNull String username);

    boolean existsByRole(@NonNull Role role);

    Page<User> findAllByEnabledFalse(@NonNull Pageable pageable);
}