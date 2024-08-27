package com.blogilf.blog.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name can not be blank.")
    @Size(max = 50, message = "Name must not exceed 50 characters.")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Username can not be blank.")
    @Size(max = 50, message = "Username must not exceed 50 characters.")
    @Column(name = "username",unique = true)
    private String username;

    @NotBlank(message = "Password can not be blank.")
    @Column(name = "password")
    private String password;

    @CreationTimestamp
    @Column(name = "date_joined", updatable = false)
    private LocalDateTime dateJoined;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Article> articles;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "location", columnDefinition = "geometry(Point, 4326)")
    @JsonIgnore
    private Point location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Country country;

    @JsonProperty("country")
    public String getCountryName() {
        return country != null ? country.getName() : "Non-country";
    }

    @JsonProperty("articles")
    public List<String> getArticleNames() {
        return articles.stream().map(Article::getTitle).collect(Collectors.toList());
    }
}
