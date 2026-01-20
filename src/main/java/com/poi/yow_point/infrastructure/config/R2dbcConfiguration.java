package com.poi.yow_point.infrastructure.config;

import io.r2dbc.postgresql.codec.PostgresqlObjectId;
import io.r2dbc.spi.ConnectionFactory;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.r2dbc.core.DatabaseClient;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class R2dbcConfiguration {

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    private final WKBReader wkbReader = new WKBReader(geometryFactory);
    private final WKBWriter wkbWriter = new WKBWriter();

    @Bean
    public R2dbcCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new GeometryToBytesConverter());
        converters.add(new BytesToGeometryConverter());
        converters.add(new PointToBytesConverter());
        converters.add(new BytesToPointConverter());
        
        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
    }

    @WritingConverter
    public static class GeometryToBytesConverter implements Converter<Geometry, byte[]> {
        private final WKBWriter wkbWriter = new WKBWriter();

        @Override
        public byte[] convert(Geometry geometry) {
            return wkbWriter.write(geometry);
        }
    }

    @ReadingConverter
    public static class BytesToGeometryConverter implements Converter<byte[], Geometry> {
        private final WKBReader wkbReader = new WKBReader();

        @Override
        public Geometry convert(byte[] bytes) {
            try {
                return wkbReader.read(bytes);
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert bytes to Geometry", e);
            }
        }
    }

    @WritingConverter
    public static class PointToBytesConverter implements Converter<Point, byte[]> {
        private final WKBWriter wkbWriter = new WKBWriter();

        @Override
        public byte[] convert(Point point) {
            return wkbWriter.write(point);
        }
    }

    @ReadingConverter
    public static class BytesToPointConverter implements Converter<byte[], Point> {
        private final WKBReader wkbReader = new WKBReader();

        @Override
        public Point convert(byte[] bytes) {
            try {
                Geometry geometry = wkbReader.read(bytes);
                if (geometry instanceof Point) {
                    return (Point) geometry;
                }
                throw new IllegalArgumentException("Expected Point geometry, got " + geometry.getGeometryType());
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert bytes to Point", e);
            }
        }
    }
}
