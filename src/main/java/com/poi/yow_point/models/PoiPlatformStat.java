package com.poi.yow_point.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "poi_platform_stat")
public class PoiPlatformStat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "stat_id", updatable = false, nullable = false)
    private UUID statId;

    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "poi_id") // Nullable as per SQL (can be overall org stats)
    private PointOfInterest pointOfInterest;

    @Column(name = "platform_type", nullable = false)
    private String platformType; // e.g. 'IOS', 'ANDROID', 'LINUX', etc.

    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate;

    @Column(name = "views", columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private Integer views = 0;

    @Column(name = "reviews", columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private Integer reviews = 0;

    @Column(name = "likes", columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private Integer likes = 0;

    @Column(name = "dislikes", columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private Integer dislikes = 0;
}
