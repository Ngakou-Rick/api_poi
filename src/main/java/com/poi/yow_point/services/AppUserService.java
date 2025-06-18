package com.poi.yow_point.services;

import com.poi.yow_point.dto.AppUserDTO;
import com.poi.yow_point.mappers.AppUserMapper;
import com.poi.yow_point.models.AppUser;
import com.poi.yow_point.models.Organization;
import com.poi.yow_point.repositories.AppUserRepository;
import com.poi.yow_point.repositories.OrganizationRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.security.crypto.password.PasswordEncoder; // If using Spring Security

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    private static final Logger log = LoggerFactory.getLogger(AppUserService.class);
    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;
    private final OrganizationRepository organizationRepository; // Added for fetching Organization

    // private final PasswordEncoder passwordEncoder; // If using Spring Security

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, AppUserMapper appUserMapper, OrganizationRepository organizationRepository) { // , PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
        this.organizationRepository = organizationRepository; // Added
        // this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AppUserDTO saveUser(AppUserDTO appUserDTO) {
        log.info("Saving user: {}", appUserDTO.getUsername());
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        
        Organization organization = organizationRepository.findById(appUserDTO.getOrgId())
            .orElseThrow(() -> new RuntimeException("Organization not found for ID: " + appUserDTO.getOrgId())); // TODO: Custom exception
        appUser.setOrganization(organization);
        
        AppUser savedUser = appUserRepository.save(appUser);
        log.info("Saved user with ID: {}", savedUser.getUserId());
        return appUserMapper.toDTO(savedUser);
    }
    
    @Transactional
    public AppUserDTO updateUser(UUID id, AppUserDTO appUserDTO) {
        log.info("Updating user with ID: {}", id);
        AppUser existingUser = appUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id)); // TODO: Custom exception

        // Use the mapper to update the entity from the DTO, ignoring null values
        appUserMapper.updateFromDto(appUserDTO, existingUser);

        if (appUserDTO.getOrgId() != null) {
            Organization organization = organizationRepository.findById(appUserDTO.getOrgId())
                .orElseThrow(() -> new RuntimeException("Organization not found for ID: " + appUserDTO.getOrgId()));
            existingUser.setOrganization(organization);
        }
        // Password update should be a separate, secure process
        
        AppUser updatedUser = appUserRepository.save(existingUser);
        log.info("Updated user with ID: {}", updatedUser.getUserId());
        return appUserMapper.toDTO(updatedUser);
    }


    public Optional<AppUserDTO> getUserById(UUID id) {
        log.info("Fetching user by ID: {}", id);
        return appUserRepository.findById(id).map(appUserMapper::toDTO);
    }

    public Optional<AppUserDTO> getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        return appUserRepository.findByUsername(username).map(appUserMapper::toDTO);
    }
    
    public Optional<AppUserDTO> getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        return appUserRepository.findByEmail(email).map(appUserMapper::toDTO);
    }

    public List<AppUserDTO> getAllUsers() {
        log.info("Fetching all users");
        return appUserRepository.findAll()
                                .stream()
                                .map(appUserMapper::toDTO)
                                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(UUID id) {
        log.info("Deleting user by ID: {}", id);
        appUserRepository.deleteById(id);
        log.info("Deleted user with ID: {}", id);
    }
}