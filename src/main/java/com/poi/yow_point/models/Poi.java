package com.poi.yow_point.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.locationtech.jts.geom.Point; // Importer le Point de JTS
// import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "points_of_interest")
@Getter
@Setter
@NoArgsConstructor
public class Poi {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID default gen_random_uuid()") // Ou
                                                                                                                   // GenerationType.SEQUENCE
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 100)
    private String country; // ex:"Cameroun"
    @Column(nullable = false, length = 100)
    private String city; // ex: "Yaoundé"

    @Column(length = 1000) // Exemple de contrainte de longueur
    private String description;

    @Column(nullable = false)
    private String category; // Ex: "restaurant", "hospital", "museum"

    // Mappage du champ géométrique pour PostGIS
    // SRID 4326 est WGS 84 (coordonnées géographiques latitude/longitude)
    @Column(name = "location", nullable = false, columnDefinition = "geometry(Point,4326)")
    private Point location;

    @Column(nullable = false)
    private String address; // Adresse formelle

    @Column(name = "informal_address", length = 500) // Adresse textuelle, optionnelle
    private String informal_address; // Adresse textuelle

    @Column
    @ElementCollection
    private List<String> catalogue = new ArrayList<>();

    @Column(name = "opening_hours")
    @ElementCollection // Pour stocker une liste de chaînes
    private List<String> openingHours = new ArrayList<>(); // Heures d'ouverture
    @Column(name = "contact_info") // Informations de contact
    private String contactInfo;
    @Column(name = "website") // Site web
    private String website;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructeur pour la création (sans ID, createdAt, updatedAt)
    public Poi(String name, String description, String category, Point location, String address) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.location = location;
        this.address = address;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
