package com.blogilf.blog.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;
    
    @Column(length = 50, unique = true, nullable = false)
    private String slug;

    @Lob
    @Column(nullable = false)
    private String content;

    private LocalDate publishedDate;
    
    @PrePersist
    protected void onCreate() {
        if (this.publishedDate == null) {
            this.publishedDate = LocalDate.now();
        }
    }
}
