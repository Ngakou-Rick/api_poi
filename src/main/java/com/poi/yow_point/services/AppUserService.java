package com.poi.yow_point.services;

import com.poi.yow_point.dto.AppUserDTO;
import com.poi.yow_point.mappers.AppUserMapper;
import com.poi.yow_point.models.AppUser;
import com.poi.yow_point.repositories.AppUserRepository;
import com.poi.yow_point.repositories.OrganizationRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class AppUserService {

    private static final Logger log = LoggerFactory.getLogger(AppUserService.class);
    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;
    private final OrganizationRepository organizationRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository,
            AppUserMapper appUserMapper,
            OrganizationRepository organizationRepository) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
        this.organizationRepository = organizationRepository;
    }

    @Transactional
    public Mono<AppUserDTO> saveUser(AppUserDTO appUserDTO) {
        log.info("Saving user: {}", appUserDTO.getUsername());

        return organizationRepository.existsById(appUserDTO.getOrgId())
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono
                                .error(new RuntimeException("Organization not found for ID: " + appUserDTO.getOrgId()));
                    }

                    AppUser appUser = appUserMapper.toEntity(appUserDTO);
                    appUser.setOrgId(appUserDTO.getOrgId()); // Assigner directement l'orgId

                    return appUserRepository.save(appUser)
                            .doOnSuccess(savedUser -> log.info("Saved user with ID: {}", savedUser.getUserId()))
                            .map(appUserMapper::toDTO);
                });
    }

    @Transactional
    public Mono<AppUserDTO> updateUser(UUID id, AppUserDTO appUserDTO) {
        log.info("Updating user with ID: {}", id);

        return appUserRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id " + id)))
                .flatMap(existingUser -> {
                    // Vérifier l'organisation si elle est fournie
                    if (appUserDTO.getOrgId() != null) {
                        return organizationRepository.existsById(appUserDTO.getOrgId())
                                .flatMap(exists -> {
                                    if (!exists) {
                                        return Mono.error(new RuntimeException(
                                                "Organization not found for ID: " + appUserDTO.getOrgId()));
                                    }

                                    // Mettre à jour l'utilisateur
                                    appUserMapper.updateFromDto(appUserDTO, existingUser);
                                    if (appUserDTO.getOrgId() != null) {
                                        existingUser.setOrgId(appUserDTO.getOrgId());
                                    }

                                    return appUserRepository.save(existingUser)
                                            .doOnSuccess(updatedUser -> log.info("Updated user with ID: {}",
                                                    updatedUser.getUserId()))
                                            .map(appUserMapper::toDTO);
                                });
                    } else {
                        // Pas de changement d'organisation
                        appUserMapper.updateFromDto(appUserDTO, existingUser);
                        return appUserRepository.save(existingUser)
                                .doOnSuccess(
                                        updatedUser -> log.info("Updated user with ID: {}", updatedUser.getUserId()))
                                .map(appUserMapper::toDTO);
                    }
                });
    }

    public Mono<AppUserDTO> getUserById(UUID id) {
        log.info("Fetching user by ID: {}", id);
        return appUserRepository.findById(id)
                .map(appUserMapper::toDTO);
    }

    public Mono<AppUserDTO> getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        return appUserRepository.findByUsername(username)
                .map(appUserMapper::toDTO);
    }

    public Mono<AppUserDTO> getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        return appUserRepository.findByEmail(email)
                .map(appUserMapper::toDTO);
    }

    public Flux<AppUserDTO> getAllUsers() {
        log.info("Fetching all users");
        return appUserRepository.findAll()
                .doOnComplete(() -> log.info("Fetched all users"))
                .map(appUserMapper::toDTO);
    }

    @Transactional
    public Mono<Void> deleteUser(UUID id) {
        log.info("Deleting user by ID: {}", id);
        return appUserRepository.deleteById(id)
                .doOnSuccess(unused -> log.info("Deleted user with ID: {}", id));
    }

    // Méthodes utilitaires supplémentaires pour la programmation réactive
    public Mono<Boolean> userExists(UUID id) {
        return appUserRepository.existsById(id);
    }

    public Mono<Boolean> usernameExists(String username) {
        return appUserRepository.findByUsername(username)
                .hasElement();
    }

    public Mono<Boolean> emailExists(String email) {
        return appUserRepository.findByEmail(email)
                .hasElement();
    }
}