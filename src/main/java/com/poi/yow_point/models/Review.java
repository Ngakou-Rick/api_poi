package com.poi.yow_point.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
// import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "poi_review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reviewId;

    @ManyToOne(fetch = FetchType.LAZY) // Charger le POI seulement si nécessaire
    @JoinColumn(name = "poi_id", nullable = false)
    @NotNull
    private Poi pointOfInterest; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Référence à l'ID de l'utilisateur (String si ID de Keycloak)
    @NotNull
    private User user; // Référence à l'entité User

    @Min(1)
    @Max(5)
    @NotNull
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String reviewText;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder.Default
    private Integer likes = 0;

    @Builder.Default
    private Integer dislikes = 0;
}
