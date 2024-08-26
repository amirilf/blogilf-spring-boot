package com.blogilf.blog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogilf.blog.model.entity.Country;
import com.blogilf.blog.service.CountryService;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    CountryController(CountryService countryService){
        this.countryService = countryService;
    }
 
    @GetMapping("")
    public List<Country> getMethodName() {
        return countryService.getCountries();
    }
    
}
