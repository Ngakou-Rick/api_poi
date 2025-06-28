package com.poi.yow_point.repositories;

import com.poi.yow_point.models.Organization;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrganizationRepository extends R2dbcRepository<Organization, UUID> {
    // Les méthodes CRUD de base sont héritées et retournent des Mono<T> et Flux<T>

    // Méthode personnalisée pour rechercher par orgCode
    Mono<Organization> findByOrgCode(String orgCode);

    // Note: Dans R2DBC, toutes les méthodes retournent des types réactifs :
    // - Mono<T> pour une seule entité ou un résultat vide
    // - Flux<T> pour une collection d'entités
    // - Mono<Void> pour les opérations sans retour (comme delete)
}