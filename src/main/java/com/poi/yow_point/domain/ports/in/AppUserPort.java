package com.poi.yow_point.application.service.appUser;

import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.AppUserDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AppUserService {

    /**
     * Crée un nouvel utilisateur
     */
    Mono<com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.AppUserDTO> saveUser(com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.AppUserDTO appUserDTO);

    /**
     * Met à jour un utilisateur existant
     */
    Mono<com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.AppUserDTO> updateUser(UUID id, com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.AppUserDTO appUserDTO);

    /**
     * Récupère un utilisateur par son ID
     */
    Mono<com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.AppUserDTO> getUserById(UUID id);

    /**
     * Récupère un utilisateur par son nom d'utilisateur
     */
    Mono<com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.AppUserDTO> getUserByUsername(String username);

    /**
     * Récupère un utilisateur par son email
     */
    Mono<com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.AppUserDTO> getUserByEmail(String email);

    /**
     * Récupère tous les utilisateurs
     */
    Flux<com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.AppUserDTO> getAllUsers();

    /**
     * Supprime un utilisateur par son ID
     */
    Mono<Void> deleteUser(UUID id);

    /**
     * Vérifie si un utilisateur existe par son ID
     */
    Mono<Boolean> userExists(UUID id);

    /**
     * Vérifie si un nom d'utilisateur existe
     */
    Mono<Boolean> usernameExists(String username);

    /**
     * Vérifie si un email existe
     */
    Mono<Boolean> emailExists(String email);

    /**
     * Récupère les utilisateurs actifs d'une organisation
     */
    Flux<com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.AppUserDTO> getActiveUsersByOrganization(UUID orgId);

    /**
     * Récupère les utilisateurs par rôle
     */
    Flux<com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.AppUserDTO> getUsersByRole(String role);

    /**
     * Compte les utilisateurs actifs d'une organisation
     */
    Mono<Long> countActiveUsersByOrganization(UUID orgId);
}