package com.poi.yow_point.config;

import com.poi.yow_point.config.json_Converter.JsonNodeToJsonConverter;
import com.poi.yow_point.config.json_Converter.JsonToJsonNodeConverter;
import com.poi.yow_point.config.postGIS_Converter.PointToStringConverter;
import com.poi.yow_point.config.postGIS_Converter.StringToPointConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class R2dbcConfig {

    /**
     * Définit UN SEUL bean pour toutes les conversions personnalisées de R2DBC.
     * Spring injectera automatiquement tous les beans qui implémentent Converter.
     */
    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions(
            StringToPointConverter stringToPointConverter,
            PointToStringConverter pointToStringConverter,
            JsonNodeToJsonConverter jsonNodeToJsonConverter,
            JsonToJsonNodeConverter jsonToJsonNodeConverter) {

        List<Converter<?, ?>> converters = new ArrayList<>();
        // Ajout des convertisseurs pour PostGIS
        converters.add(stringToPointConverter);
        converters.add(pointToStringConverter);

        // Ajout des convertisseurs pour JsonNode <-> Json
        converters.add(jsonNodeToJsonConverter);
        converters.add(jsonToJsonNodeConverter);

        // Crée et retourne la configuration avec tous les convertisseurs
        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
    }
}