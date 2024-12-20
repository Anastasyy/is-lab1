package itmo.is.service.security.authentication;

import itmo.is.dto.authentication.LoginRequest;
import itmo.is.dto.authentication.JwtResponse;
import itmo.is.dto.authentication.RegisterRequest;
import itmo.is.dto.authentication.UserDto;
import itmo.is.mapper.security.UserMapper;
import itmo.is.model.security.Role;
import itmo.is.model.security.User;
import itmo.is.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${application.security.password-min-length}")
    private int passwordMinLength;

    public JwtResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + request.username()));

        if (!user.isEnabled()) {
            throw new AuthenticationServiceException("User is disabled");
        }

        return generateJwt(user);
    }

    public JwtResponse registerUser(RegisterRequest request) {
        return registerEnabled(request, Role.ROLE_USER);
    }

    public boolean hasRegisteredAdmins() {
        return userRepository.existsByRole(Role.ROLE_ADMIN);
    }

    public JwtResponse registerFirstAdmin(RegisterRequest request) {
        if (hasRegisteredAdmins()) {
            throw new AuthenticationServiceException("First admin is already registered");
        }
        return registerEnabled(request, Role.ROLE_ADMIN);
    }

    public void submitAdminRegistrationRequest(RegisterRequest registerRequest) {
        if (!hasRegisteredAdmins()) {
            throw new AuthenticationServiceException("First admin is not registered yet");
        }
        boolean enabled = false;
        createUser(registerRequest, Role.ROLE_ADMIN, enabled);
    }

    public void approveAdminRegistrationRequest(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void rejectAdminRegistrationRequest(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        if (user.isEnabled()) {
            throw new AuthenticationServiceException("User is enabled");
        }
        userRepository.delete(user);
    }

    public Page<UserDto> getPendingRegistrationRequests(Pageable pageable) {
        return userRepository.findAllByEnabledFalse(pageable).map(userMapper::toDto);
    }

    private JwtResponse registerEnabled(RegisterRequest request, Role role) {
        boolean enabled = true;
        var user = createUser(request, role, enabled);
        return generateJwt(user);
    }

    private User createUser(RegisterRequest request, Role role, boolean enabled) {
        validateRegisterRequest(request);
        User user = userMapper.toEntity(request);
        encodePassword(user);
        user.setRole(role);
        user.setEnabled(enabled);
        return userRepository.save(user);
    }

    private void validateRegisterRequest(RegisterRequest request) {
        checkIfUsernameIsTaken(request.username());
        validatePasswordLength(request.password());
    }

    private void validatePasswordLength(String password) {
        if (password.length() < passwordMinLength) {
            throw new AuthenticationServiceException("Password must be at least " + passwordMinLength + " characters");
        }
    }

    private void encodePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    private void checkIfUsernameIsTaken(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new AuthenticationServiceException("Username " + username + " is taken");
        }
    }

    private JwtResponse generateJwt(User user) {
        String jwt = jwtService.generateToken(user);
        return new JwtResponse(jwt);
    }
}
