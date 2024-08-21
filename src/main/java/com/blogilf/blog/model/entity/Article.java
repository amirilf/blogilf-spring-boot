package com.blogilf.blog.model.entity;

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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

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

    @Default
    @NotNull(message = "View cannot be null.")
    @PositiveOrZero(message = "View cannot be less than 0.")
    @Column(name = "view")
    private Long view = 0L;

    @NotNull(message = "Read time cannot be null.")
    @Min(value = 1, message = "Reading an article at least takes 1 min.")
    @Column(name = "readtime")
    private int readTime;

    @Override
    public String toString(){
        return title + " " + content + " " + slug + " " + publishedDate;
    }
}
