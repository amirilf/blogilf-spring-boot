package com.blogilf.blog.model.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.blogilf.blog.model.entity.Article;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class ArticleSpecification {
 
    public static Specification<Article> hasTitle(String title) {
        return (Root<Article> obj, CriteriaQuery<?> query, CriteriaBuilder builder) ->
            builder.like(obj.get("title"), "%" + title + "%");
    }

    public static Specification<Article> hasSlug(String slug) {
        return (Root<Article> obj, CriteriaQuery<?> query, CriteriaBuilder builder) ->
            builder.like(obj.get("slug"), "%" + slug + "%");
    }

    public static Specification<Article> hasContent(String content) {
        return (Root<Article> obj, CriteriaQuery<?> query, CriteriaBuilder builder) ->
            builder.like(obj.get("content"), "%" + content + "%");
    }

    
    //---> publishedDate
    public static Specification<Article> publishedDateBetween(LocalDate startDate, LocalDate endDate) {
        return (Root<Article> obj, CriteriaQuery<?> query, CriteriaBuilder builder) ->
            builder.between(obj.get("publishedDate"), startDate, endDate);
    }

    public static Specification<Article> publishedDateAfter(LocalDate date) {
        return (Root<Article> obj, CriteriaQuery<?> query, CriteriaBuilder builder) ->
            builder.greaterThanOrEqualTo(obj.get("publishedDate"), date);
    }

    public static Specification<Article> publishedDateBefore(LocalDate date) {
        return (Root<Article> obj, CriteriaQuery<?> query, CriteriaBuilder builder) ->
            builder.lessThanOrEqualTo(obj.get("publishedDate"), date);
    }
    // End publishedDate


    //---> view
    public static Specification<Article> viewBetween(Long minView, Long maxView) {
        return (Root<Article> obj, CriteriaQuery<?> query, CriteriaBuilder builder) ->
            builder.between(obj.get("view"), minView, maxView);
    }

    public static Specification<Article> viewLessThan(Long view) {
        return (Root<Article> obj, CriteriaQuery<?> query, CriteriaBuilder builder) ->
            builder.lessThan(obj.get("view"), view);
    }

    public static Specification<Article> viewGreaterThan(Long view) {
        return (Root<Article> obj, CriteriaQuery<?> query, CriteriaBuilder builder) ->
            builder.greaterThan(obj.get("view"), view);
    }
    // End view


    //---> readtTme
    public static Specification<Article> readTimeBetween(int minReadTime, int maxReadTime) {
        return (Root<Article> obj, CriteriaQuery<?> query, CriteriaBuilder builder) ->
            builder.between(obj.get("readTime"), minReadTime, maxReadTime);
    }

    public static Specification<Article> readTimeLessThan(int readTime) {
        return (Root<Article> obj, CriteriaQuery<?> query, CriteriaBuilder builder) ->
            builder.lessThan(obj.get("readTime"), readTime);
    }

    public static Specification<Article> readTimeGreaterThan(int readTime) {
        return (Root<Article> obj, CriteriaQuery<?> query, CriteriaBuilder builder) ->
            builder.greaterThan(obj.get("readTime"), readTime);
    }
    // end readTime
    
}
