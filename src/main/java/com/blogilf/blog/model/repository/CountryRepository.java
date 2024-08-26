package com.blogilf.blog.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogilf.blog.model.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}