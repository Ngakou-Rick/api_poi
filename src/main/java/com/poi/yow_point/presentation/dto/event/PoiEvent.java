package com.poi.yow_point.presentation.dto.event;

import com.poi.yow_point.presentation.dto.PointOfInterestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoiEvent {

    private EventType type;
    private PointOfInterestDTO poi;

    public enum EventType {
        CREATED,
        UPDATED,
        DELETED
    }
}
