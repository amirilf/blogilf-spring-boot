package com.blogilf.blog.model.repository;

import java.util.Optional;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.blogilf.blog.model.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query("SELECT c FROM Country c WHERE ST_Contains(c.polygon, :location) = true")
    Optional<Country> findCountryByLocation(Point location);
}