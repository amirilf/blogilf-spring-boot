package com.blogilf.blog.model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Title can not be null.")
    @Size(max = 50, message = "Title must not exceed 50 characters.")
    @Column(name="title")
    private String title;
    
    @NotNull(message = "Slug can not be null.")
    @Size(max = 50, message = "Slug must not exceed 50 characters.")
    @Column(name = "slug", unique = true)
    private String slug;

    @NotNull(message = "Content can not be null.")
    @Lob
    @Column(name = "content")
    private String content;

    @CreationTimestamp
    @Column(name = "published_date", updatable = true)
    private LocalDate publishedDate;

    @NotNull(message = "User can not be null.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User author;
    
}
