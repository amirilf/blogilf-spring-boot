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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title can not be blank.")
    @Size(max = 50, message = "Title must not exceed 50 characters.")
    @Column(name="title")
    private String title;
    
    @NotBlank(message = "Slug can not be blank.")
    @Size(max = 50, message = "Slug must not exceed 50 characters.")
    @Column(name = "slug", unique = true)
    private String slug;

    @NotBlank(message = "Content can not be blank.")
    @Lob
    @Column(name = "content")
    private String content;

    @CreationTimestamp
    @Column(name = "published_date", updatable = true)
    private LocalDate publishedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User author;
    
    @Override
    public String toString(){
        return title + " " + content + " " + slug + " " + publishedDate;
    }
}
