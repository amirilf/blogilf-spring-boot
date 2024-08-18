package com.blogilf.blog.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogilf.blog.service.SearchService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping("/search")
public class SearchController {
    
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }
    
    @GetMapping("")
    public ResponseEntity<? extends Page<?>> searchArticles (
        @RequestParam @NotBlank(message = "Query cannot be blank") String query,
        @RequestParam(defaultValue = "articles") @Pattern(regexp = "articles|users", message = "Type must be 'articles' or 'users'") String type,
        @RequestParam(defaultValue = "nto") @Pattern(regexp = "nto|otn", message = "Sort must be 'nto' or 'otn'") String sort,
        @RequestParam(defaultValue = "1") @Min(1) int page) {
        
        return searchService.search(query, type, sort, page);
    }
}
