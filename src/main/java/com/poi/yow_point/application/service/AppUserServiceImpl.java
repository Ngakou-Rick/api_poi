package com.poi.yow_point.application.service;

import com.poi.yow_point.domain.model.AppUser;
import com.poi.yow_point.infrastructure.mappers.AppUserMapper;
import com.poi.yow_point.domain.ports.in.AppUserPort;
import com.poi.yow_point.infrastructure.adapters.inbound.rest.validation.AppUserValidator;
import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.AppUserEntity;
//import com.poi.yow_point.infrastructure.adapters.outbound.persistence.repository.OrganizationRepository;
import com.poi.yow_point.domain.ports.out.AppUserRepositoryPort;
import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.AppUserDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class AppUserServiceImpl implements AppUserPort {

    private static final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final AppUserRepositoryPort appUserRepository;
    private final AppUserMapper appUserMapper;
    private final AppUserValidator validationService;
    private final PasswordEncoder passwordEncoder;

    public AppUserServiceImpl(AppUserRepositoryPort appUserRepository,
            AppUserMapper appUserMapper,
            AppUserValidator validationService,
            PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
        this.validationService = validationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Mono<AppUserDTO> saveUser(AppUserDTO appUserDTO) {
        log.info("Saving user: {}", appUserDTO.getUsername());

        return validationService.validateForCreation(appUserDTO)
                .then(Mono.fromCallable(() -> {
                    // Hasher le mot de passe
                    String hashedPassword = passwordEncoder.encode(appUserDTO.getPassword());

                    // Créer l'entité domaine avec le mot de passe hashé
                    AppUserEntity tempEntity = appUserMapper.toEntity(appUserDTO);
                    AppUser appUser = new AppUser(
                        null, // userId sera généré par la base
                        tempEntity.getOrgId(),
                        tempEntity.getUsername(),
                        tempEntity.getEmail(),
                        tempEntity.getPhone(),
                        hashedPassword,
                        tempEntity.getRole(),
                        tempEntity.getIsActive(),
                        tempEntity.getCreatedAt()
                    );

                    return appUser;
                }))
                .flatMap(appUserRepository::save)
                .doOnSuccess(savedUser -> log.info("Saved user with ID: {}", savedUser.userId()))
                .map(appUserMapper::toDTO);
    }

    @Override
    @Transactional
    public Mono<AppUserDTO> updateUser(UUID id, AppUserDTO appUserDTO) {
        log.info("Updating user with ID: {}", id);

        return validationService.validateForUpdate(id, appUserDTO)
                .then(appUserRepository.findById(id))
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id " + id)))
                .flatMap(existingUser -> {
                    // Créer une entité temporaire pour la mise à jour
                    AppUserEntity tempEntity = appUserMapper.toEntity(existingUser);
                    appUserMapper.updateFromDto(appUserDTO, tempEntity);

                    // Hasher le nouveau mot de passe si fourni
                    String passwordHash = existingUser.passwordHash();
                    if (appUserDTO.getPassword() != null && !appUserDTO.getPassword().trim().isEmpty()) {
                        passwordHash = passwordEncoder.encode(appUserDTO.getPassword());
                    }

                    // Créer le nouvel AppUser avec les données mises à jour
                    AppUser updatedUser = new AppUser(
                        existingUser.userId(),
                        tempEntity.getOrgId(),
                        tempEntity.getUsername(),
                        tempEntity.getEmail(),
                        tempEntity.getPhone(),
                        passwordHash,
                        tempEntity.getRole(),
                        tempEntity.getIsActive(),
                        tempEntity.getCreatedAt()
                    );

                    return appUserRepository.save(updatedUser);
                })
                .doOnSuccess(updatedUser -> log.info("Updated user with ID: {}", updatedUser.userId()))
                .map(appUserMapper::toDTO);
    }

    @Override
    public Mono<AppUserDTO> getUserById(UUID id) {
        log.info("Fetching user by ID: {}", id);
        return appUserRepository.findById(id)
                .map(appUserMapper::toDTO);
    }

    @Override
    public Mono<AppUserDTO> getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        return appUserRepository.findByUsername(username)
                .map(appUserMapper::toDTO);
    }

    @Override
    public Mono<AppUserDTO> getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        return appUserRepository.findByEmail(email)
                .map(appUserMapper::toDTO);
    }

    @Override
    public Flux<AppUserDTO> getAllUsers() {
        log.info("Fetching all users");
        return appUserRepository.findAll()
                .doOnComplete(() -> log.info("Fetched all users"))
                .map(appUserMapper::toDTO);
    }

    @Override
    @Transactional
    public Mono<Void> deleteUser(UUID id) {
        log.info("Deleting user by ID: {}", id);
        return appUserRepository.deleteById(id)
                .doOnSuccess(unused -> log.info("Deleted user with ID: {}", id));
    }

    @Override
    public Mono<Boolean> userExists(UUID id) {
        return appUserRepository.existsById(id);
    }

    @Override
    public Mono<Boolean> usernameExists(String username) {
        return appUserRepository.findByUsername(username)
                .hasElement();
    }

    @Override
    public Mono<Boolean> emailExists(String email) {
        return appUserRepository.findByEmail(email)
                .hasElement();
    }

    @Override
    public Flux<AppUserDTO> getActiveUsersByOrganization(UUID orgId) {
        log.info("Fetching active users for organization: {}", orgId);
        return appUserRepository.findByOrgIdAndIsActive(orgId, true)
                .map(appUserMapper::toDTO);
    }

    @Override
    public Flux<AppUserDTO> getUsersByRole(String role) {
        log.info("Fetching users with role: {}", role);
        return appUserRepository.findByRole(role)
                .map(appUserMapper::toDTO);
    }

    @Override
    public Mono<Long> countActiveUsersByOrganization(UUID orgId) {
        return appUserRepository.countActiveUsersByOrgId(orgId);
    }
}