package com.blogilf.blog.model.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blogilf.blog.model.entity.User;

public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    Page<User> findByNameContainingOrUsernameContaining(String name, String username, Pageable pageable);
}
