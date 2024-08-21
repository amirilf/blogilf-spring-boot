package com.blogilf.blog.model.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogilf.blog.model.entity.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request,Long>{
    
    List<Request> findByTimeBetween(LocalDateTime start, LocalDateTime end);
}
