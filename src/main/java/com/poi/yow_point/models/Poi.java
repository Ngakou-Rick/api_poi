package com.poi.yow_point.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.locationtech.jts.geom.Point; // Importer le Point de JTS

import com.poi.yow_point.models.embeddable.AddressType;
import com.poi.yow_point.models.embeddable.ContactPersonType;
import io.hypersistence.utils.hibernate.type.array.ListArrayType; // Pour TEXT[]
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType; // Pour JSONB
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type; // Hibernate 6
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Point; // De hibernate-spatial / JTS


import java.util.Map; // Pour JSONB

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "point_of_interest")
public class Poi {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "poi_id")
    private UUID poiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    @NotNull
    private Organization organization;

    @Column(name = "town_id") // Pourrait être une relation @ManyToOne si Town est une entité
    private UUID townId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id") // Clé étrangère vers AppUser
    private AppUser createdByUser;

    @Column(name = "poi_name", nullable = false)
    private String poiName;

    @Column(name = "poi_type", nullable = false)
    private String poiType;

    @Column(name = "poi_category", nullable = false)
    private String poiCategory;

    @Column(name = "poi_long_name")
    private String poiLongName;

    @Column(name = "poi_short_name")
    private String poiShortName;

    @Column(name = "poi_friendly_name")
    private String poiFriendlyName;

    @Column(name = "poi_description", columnDefinition = "TEXT")
    private String poiDescription;

    @Lob // Pour les données binaires volumineuses
    @Column(name = "poi_logo")
    private byte[] poiLogo; // BYTEA

    @Type(ListArrayType.class) // Utilise hibernate-types pour mapper TEXT[] à List<String>
    @Column(name = "poi_images", columnDefinition = "text[]")
    private List<String> poiImages;

    // Utilisation de l'Embeddable pour address_type
    @Embedded
    @AttributeOverrides({ // Nécessaire si les noms de colonnes dans AddressType ne correspondent pas exactement
            @AttributeOverride(name = "streetNumber", column = @Column(name = "addr_street_number")), // Exemple de préfixe
            // ... autres si AddressType est utilisé ailleurs avec d'autres noms de colonnes
    })
    private AddressType poiAddress; // Le nom du champ correspond au nom de colonne poi_address

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "website_url")
    private String websiteUrl;

    @Type(ListArrayType.class)
    @Column(name = "poi_amenities", columnDefinition = "text[]")
    private List<String> poiAmenities;

    @Type(ListArrayType.class)
    @Column(name = "poi_keywords", columnDefinition = "text[]")
    private List<String> poiKeywords;

    @Type(ListArrayType.class)
    @Column(name = "poi_type_tags", columnDefinition = "text[]")
    private List<String> poiTypeTags;

    @Type(JsonBinaryType.class) // Utilise hibernate-types pour JSONB
    @Column(name = "operation_time_plan", columnDefinition = "jsonb")
    private Map<String, String> operationTimePlan; // Ou une classe plus structurée

    // Utilisation de l'Embeddable pour la liste de contact_person_type[]
    // C'est un peu plus complexe pour un tableau d'@Embeddable.
    // Option 1: Utiliser JSONB pour stocker une liste d'objets contact.
    @Type(JsonBinaryType.class)
    @Column(name = "poi_contacts", columnDefinition = "jsonb")
    private List<ContactPersonType> poiContacts; // Stocke une liste de ContactPersonType sérialisée en JSON

    // Option 2 (plus complexe): Créer une UserType Hibernate pour contact_person_type[]
    // Ou mapper vers une table séparée si la normalisation est préférée (comme vous l'avez fait pour poi_contact).

    @Column(name = "popularity_score")
    @Builder.Default
    private Float popularityScore = 0.0f;

    @NotNull
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "deactivation_reason", columnDefinition = "TEXT")
    private String deactivationReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deactivated_by_user_id")
    private AppUser deactivatedByUser;

    @Column(name = "location_geog", nullable = false, columnDefinition = "geography(Point,4326)")
    private Point locationGeog; // Type de JTS (hibernate-spatial)

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_user_id")
    private AppUser updatedByUser;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relations inverses (optionnel, pour un accès facile depuis POI)
    @OneToMany(mappedBy = "pointOfInterest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PoiReview> reviews;
}