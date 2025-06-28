package com.poi.yow_point.repositories;

import com.poi.yow_point.models.AppUser;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface AppUserRepository extends R2dbcRepository<AppUser, UUID> {

    Mono<AppUser> findByUsername(String username);

    Mono<AppUser> findByEmail(String email);

    // Requête personnalisée pour vérifier l'existence d'un utilisateur par
    // organisation
    @Query("SELECT EXISTS(SELECT 1 FROM app_user WHERE org_id = :orgId)")
    Mono<Boolean> existsByOrgId(UUID orgId);

    // Requête pour compter les utilisateurs actifs d'une organisation
    @Query("SELECT COUNT(*) FROM app_user WHERE org_id = :orgId AND is_active = true")
    Mono<Long> countActiveUsersByOrgId(UUID orgId);
}