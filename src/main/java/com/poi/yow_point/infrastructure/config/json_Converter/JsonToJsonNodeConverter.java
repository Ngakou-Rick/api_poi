package com.poi.yow_point.infrastructure.config.json_Converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
@RequiredArgsConstructor
public class JsonToJsonNodeConverter implements Converter<Json, JsonNode> {

    private final ObjectMapper objectMapper;

    @Override
    public JsonNode convert(Json source) {
        if (source == null) {
            return null;
        }
        try {
            String jsonString = source.asString();
            if (jsonString == null || jsonString.trim().isEmpty() || "null".equals(jsonString.trim())) {
                return null;
            }
            return objectMapper.readTree(jsonString);
        } catch (Exception e) {
            System.err.println("Erreur lors de la conversion Json vers JsonNode: " + e.getMessage());
            return null; // Ou levez une exception
        }
    }
}