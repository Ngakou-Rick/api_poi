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
public class OrganizationDTO {
    private UUID organizationId;
    private String orgName;
    private String orgCode;
    private String orgType;
    private OffsetDateTime createdAt; // Retiré le final pour permettre la sérialisation/désérialisation
    private Boolean isActive;

    // Note: Les collections d'entités liées (users, pois, etc.) ne sont
    // généralement pas incluses
    // dans les DTOs de base pour éviter des charges utiles trop importantes et des
    // dépendances circulaires.
    // Dans un contexte réactif, ces relations peuvent être chargées via des
    // endpoints séparés
    // ou des méthodes de service dédiées retournant des Flux/Mono.
}