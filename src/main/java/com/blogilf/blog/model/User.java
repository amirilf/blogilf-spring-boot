package com.blogilf.blog.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

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

    @NotNull(message = "Name can not be null.")
    @Size(max = 50, message = "Name must not exceed 50 characters.")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Username can not be null.")
    @Size(max = 50, message = "Username must not exceed 50 characters.")
    @Column(name = "username",unique = true)
    private String username;

    @NotNull(message = "Password can not be null.")
    @Column(name = "password")
    private String password;

    @CreationTimestamp
    @Column(name = "date_joined", updatable = false)
    private LocalDateTime dateJoined;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Article> articles;

}
