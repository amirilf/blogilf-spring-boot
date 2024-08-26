package com.blogilf.blog.service;

import org.springframework.stereotype.Service;

import com.blogilf.blog.model.entity.Country;
import com.blogilf.blog.model.repository.CountryRepository;

import java.util.List;

@Service
public class CountryService {
    
    private final CountryRepository countryRepository;

    CountryService(CountryRepository countryRepository){
        this.countryRepository = countryRepository;
    }

    public List<Country> getCountries() {
        return countryRepository.findAll();
    }
}
