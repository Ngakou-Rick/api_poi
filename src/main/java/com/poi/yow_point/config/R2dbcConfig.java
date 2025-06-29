package com.poi.yow_point.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
//import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    private final ObjectMapper objectMapper;

    @Override
    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new JsonNodeToJsonConverter(objectMapper));
        converters.add(new JsonToJsonNodeConverter(objectMapper));
        return new R2dbcCustomConversions(getStoreConversions(), converters);
    }

    /**
     * Convertisseur pour écrire JsonNode vers JSON PostgreSQL
     */
    @WritingConverter
    public static class JsonNodeToJsonConverter implements Converter<JsonNode, Json> {

        private final ObjectMapper objectMapper;

        public JsonNodeToJsonConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Json convert(JsonNode source) {
            if (source == null || source.isNull()) {
                return Json.of("null");
            }

            try {
                String jsonString = objectMapper.writeValueAsString(source);
                return Json.of(jsonString);
            } catch (JsonProcessingException e) {
                log.error("Erreur lors de la conversion JsonNode vers Json: {}", e.getMessage());
                return Json.of("null");
            }
        }
    }

    /**
     * Convertisseur pour lire JSON PostgreSQL vers JsonNode
     */
    @ReadingConverter
    public static class JsonToJsonNodeConverter implements Converter<Json, JsonNode> {

        private final ObjectMapper objectMapper;

        public JsonToJsonNodeConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

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
                log.error("Erreur lors de la conversion Json vers JsonNode: {}", e.getMessage());
                return null;
            }
        }
    }

    @Override
    public ConnectionFactory connectionFactory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'connectionFactory'");
    }
}