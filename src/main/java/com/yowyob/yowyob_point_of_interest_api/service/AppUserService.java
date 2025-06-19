package com.yowyob.yowyob_point_of_interest_api.service;

import com.yowyob.yowyob_point_of_interest_api.dto.AppUserDTO;
import com.yowyob.yowyob_point_of_interest_api.mapper.AppUserMapper;
import com.yowyob.yowyob_point_of_interest_api.model.AppUser;
// Organization model not needed here if only dealing with orgId
import com.yowyob.yowyob_point_of_interest_api.repository.AppUserRepository;
// OrganizationRepository not strictly needed if AppUser entity stores orgId directly and AppUserMapper handles it
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class AppUserService {

    private static final Logger log = LoggerFactory.getLogger(AppUserService.class);
    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;
    // private final OrganizationRepository organizationRepository; // Removed for now, orgId is in DTO

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, AppUserMapper appUserMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
    }

    @Transactional
    public Mono<AppUserDTO> saveUser(AppUserDTO appUserDTO) {
        log.info("Saving user: {}", appUserDTO.getUsername());
        AppUser appUser = appUserMapper.toEntity(appUserDTO);

        // orgId is directly in AppUser entity from AppUserDTO via mapper
        if (appUser.getUserId() == null) { // New User
            appUser.setUserId(UUID.randomUUID());
            appUser.setCreatedAt(OffsetDateTime.now());
             if (appUser.getIsActive() == null) {
                appUser.setIsActive(true);
            }
        }
        // TODO: Password encoding
        return appUserRepository.save(appUser)
            .map(appUserMapper::toDTO)
            .doOnSuccess(dto -> log.info("Saved user with ID: {}", dto.getUserId()))
            .doOnError(e -> log.error("Error saving user: {}", appUserDTO.getUsername(), e));
    }

    @Transactional
    public Mono<AppUserDTO> updateUser(UUID id, AppUserDTO appUserDTO) {
        log.info("Updating user with ID: {}", id);
        return appUserRepository.findById(id)
            .flatMap(existingUser -> {
                existingUser.setUsername(appUserDTO.getUsername());
                existingUser.setEmail(appUserDTO.getEmail());
                existingUser.setPhone(appUserDTO.getPhone());
                existingUser.setRole(appUserDTO.getRole());
                if (appUserDTO.getIsActive() != null) {
                    existingUser.setIsActive(appUserDTO.getIsActive());
                }
                if (appUserDTO.getOrgId() != null) { // Allow updating orgId
                    existingUser.setOrgId(appUserDTO.getOrgId());
                }
                // Password update should be separate
                return appUserRepository.save(existingUser);
            })
            .map(appUserMapper::toDTO)
            .doOnSuccess(dto -> log.info("Updated user with ID: {}", dto.getUserId()))
            .doOnError(e -> log.error("Error updating user ID: {}", id, e));
    }

    public Mono<AppUserDTO> getUserById(UUID id) {
        log.info("Fetching user by ID: {}", id);
        return appUserRepository.findById(id).map(appUserMapper::toDTO);
    }

    public Mono<AppUserDTO> getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        return appUserRepository.findByUsername(username).map(appUserMapper::toDTO);
    }

    public Mono<AppUserDTO> getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        return appUserRepository.findByEmail(email).map(appUserMapper::toDTO);
    }

    public Flux<AppUserDTO> getAllUsers() {
        log.info("Fetching all users");
        return appUserRepository.findAll().map(appUserMapper::toDTO);
    }

    @Transactional
    public Mono<Void> deleteUser(UUID id) {
        log.info("Deleting user by ID: {}", id);
        return appUserRepository.deleteById(id)
            .doOnSuccess(v -> log.info("Deleted user with ID: {}", id))
            .doOnError(e -> log.error("Error deleting user ID: {}", id, e));
    }
}
