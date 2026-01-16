package com.poi.yow_point.infrastructure.adapters.inbound.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PoiAccessLogDTO {

    private UUID accessId;
    private UUID poiId;
    private UUID organizationId;
    private String platformType;
    private UUID userId;
    private String accessType;
    private OffsetDateTime accessDatetime;
    private Map<String, Object> metadata;

    // Getters and Setters
    public UUID getAccessId() { return accessId; }
    public void setAccessId(UUID accessId) { this.accessId = accessId; }
    public UUID getPoiId() { return poiId; }
    public void setPoiId(UUID poiId) { this.poiId = poiId; }
    public UUID getOrganizationId() { return organizationId; }
    public void setOrganizationId(UUID organizationId) { this.organizationId = organizationId; }
    public String getPlatformType() { return platformType; }
    public void setPlatformType(String platformType) { this.platformType = platformType; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getAccessType() { return accessType; }
    public void setAccessType(String accessType) { this.accessType = accessType; }
    public OffsetDateTime getAccessDatetime() { return accessDatetime; }
    public void setAccessDatetime(OffsetDateTime accessDatetime) { this.accessDatetime = accessDatetime; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

}