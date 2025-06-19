package com.yowyob.yowyob_point_of_interest_api.model;

// Remove jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("poi_platform_stat")
public class PoiPlatformStat {

    @Id
    @Column("stat_id")
    private UUID statId;

    @Column("org_id")
    private UUID orgId;

    @Column("poi_id")
    private UUID poiId;

    @Column("platform_type")
    private String platformType;

    @Column("stat_date")
    private LocalDate statDate;

    @Column("views")
    private Integer views;

    @Column("reviews")
    private Integer reviews;

    @Column("likes")
    private Integer likes;

    @Column("dislikes")
    private Integer dislikes;
}
