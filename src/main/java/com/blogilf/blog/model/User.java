package com.blogilf.blog.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

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

    @NotNull(message = "Name cannot be null.")
    @Size(max = 50, message = "Name must not exceed 50 characters.")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Username cannot be null.")
    @Size(max = 50, message = "Username must not exceed 50 characters.")
    @Column(name = "username",unique = true)
    private String username;

    @NotNull(message = "Password cannot be null.")
    @Column(name = "password")
    private String password;

    @PastOrPresent(message = "The date joined must be in the past or present.")
    @CreationTimestamp
    @Column(name = "date_joined", updatable = false)
    private LocalDateTime dateJoined;
}
