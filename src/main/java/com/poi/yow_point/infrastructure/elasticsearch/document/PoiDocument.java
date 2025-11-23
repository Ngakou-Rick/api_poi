package com.poi.yow_point.infrastructure.elasticsearch.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "poi")
public class PoiDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private UUID createdByUserId;

    @Field(type = FieldType.Keyword)
    private UUID organizationId;

    @Field(type = FieldType.Text, name = "name")
    private String poiName;

    @Field(type = FieldType.Keyword)
    private String poiType;

    @Field(type = FieldType.Keyword)
    private String poiCategory;

    @Field(type = FieldType.Text, name = "description")
    private String poiDescription;

    @GeoPointField
    private GeoPoint location;

    @Field(type = FieldType.Text, name = "city")
    private String addressCity;

    @Field(type = FieldType.Keyword)
    private List<String> poiKeywords;

    @Field(type = FieldType.Float)
    private Float popularityScore;

    @Field(type = FieldType.Boolean)
    private Boolean isActive;

    @Field(type = FieldType.Date)
    private Instant createdAt;

    @Field(type = FieldType.Date)
    private Instant updatedAt;
}
