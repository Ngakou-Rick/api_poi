package com.poi.yow_point.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {
    private UUID userId;
    private UUID orgId; // Pour représenter la relation avec l'organisation
    private String username;
    private String email;
    private String phone;

    // Le hash du mot de passe ne doit pas être exposé dans les DTOs côté client
    // Champ pour le mot de passe en clair (seulement pour les opérations
    // d'écriture)
    // private String password; // Ne sera jamais renvoyé au client lors des
    // lectures

    private String role;
    private Boolean isActive;
    private OffsetDateTime createdAt;

    // Note: Dans un contexte réactif, les collections d'entités liées peuvent être
    // récupérées via des endpoints séparés ou des méthodes de service dédiées
    // qui retournent des Flux<T> ou Mono<T>
}