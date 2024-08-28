package com.blogilf.blog.model;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.blogilf.blog.model.entity.Country;
import com.blogilf.blog.model.repository.CountryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

@Component
@Order(1)
public class LoadCountries implements CommandLineRunner {

    private final CountryRepository countryRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private final String path = getClass().getClassLoader().getResource("countries.json").getFile();

    LoadCountries(CountryRepository countryRepository){
        this.countryRepository = countryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadCountriesFromJson();
        System.out.println("Countries loaded!");
    }
    
    private void loadCountriesFromJson() throws IOException {
    
        File jsonFile = new File(path);
        JsonNode root = new ObjectMapper().readTree(jsonFile);
        List<Country> countries = new ArrayList<>();

        for (JsonNode countryNode : root) {
            
            String name = countryNode.get("name").asText();
            JsonNode geometryNode = countryNode.get("geo_shape").get("geometry");
            
            String type = geometryNode.get("type").asText();
            JsonNode coordinatesNode = geometryNode.get("coordinates");
            
            MultiPolygon multiPolygon = null;

            if ("Polygon".equals(type)) {
                Polygon polygon = parsePolygon(coordinatesNode);
                multiPolygon = geometryFactory.createMultiPolygon(new Polygon[]{polygon});
            } else if ("MultiPolygon".equals(type)) {
                multiPolygon = parseMultiPolygon(coordinatesNode);
            }

            if (multiPolygon != null) {
                countries.add(Country.builder().name(name).polygon(multiPolygon).build());
            }
        }

        countryRepository.saveAll(countries);
    }

    private Polygon parsePolygon(JsonNode coordinatesNode) {
        
        JsonNode exteriorRing = coordinatesNode.get(0);
        Coordinate[] exteriorCoordinates = new Coordinate[exteriorRing.size() + 1]; // +1 to close polygon

        for (int i = 0; i < exteriorRing.size(); i++) {
            JsonNode coord = exteriorRing.get(i);
            double lon = coord.get(0).asDouble();
            double lat = coord.get(1).asDouble();
            exteriorCoordinates[i] = new Coordinate(lon, lat);
        }

        // first and last items must be the same!
        exteriorCoordinates[exteriorRing.size()] = exteriorCoordinates[0];

        return geometryFactory.createPolygon(exteriorCoordinates);
    }

    private MultiPolygon parseMultiPolygon(JsonNode coordinatesNode) {
        
        List<Polygon> polygons = new ArrayList<>();

        for (JsonNode polygonNode : coordinatesNode) {
            List<Coordinate[]> rings = new ArrayList<>();

            for (JsonNode ringNode : polygonNode) {
                Coordinate[] ringCoordinates = new Coordinate[ringNode.size() + 1];
                for (int i = 0; i < ringNode.size(); i++) {
                    JsonNode coord = ringNode.get(i);
                    double lon = coord.get(0).asDouble();
                    double lat = coord.get(1).asDouble();
                    ringCoordinates[i] = new Coordinate(lon, lat);
                }
                ringCoordinates[ringNode.size()] = ringCoordinates[0];
                rings.add(ringCoordinates);
            }

            Polygon polygon = geometryFactory.createPolygon(rings.get(0));
            polygons.add(polygon);
        }

        return geometryFactory.createMultiPolygon(polygons.toArray(new Polygon[0]));
    }
}
