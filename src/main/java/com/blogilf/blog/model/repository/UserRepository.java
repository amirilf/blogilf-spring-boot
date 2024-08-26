package com.blogilf.blog.model.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blogilf.blog.model.entity.User;
import com.blogilf.blog.model.projection.UserDistanceProjection;

public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    Page<User> findByNameContainingOrUsernameContaining(String name, String username, Pageable pageable);

    @Query(
        value = "SELECT ST_DistanceSphere(u1.location, u2.location) / 1000 AS distance_km, " +
                   "ST_X(u1.location) AS user1_lon, ST_Y(u1.location) AS user1_lat, " +
                   "ST_X(u2.location) AS user2_lon, ST_Y(u2.location) AS user2_lat " +
                   "FROM users u1, users u2 " +
                   "WHERE u1.username = :curUsername AND u2.username = :distUsername", 
        nativeQuery = true
    )
    UserDistanceProjection findDistanceAndLocations(@Param("curUsername") String curUsername, @Param("distUsername") String distUsername);

    @Query(
        value = "SELECT u.*, (ST_DistanceSphere(u.location, (SELECT location FROM users WHERE username = :curUsername)) / 1000) as distance " +
                "FROM users u " +
                "WHERE u.role = 'USER' AND u.username != :curUsername " +
                "AND (ST_DistanceSphere(u.location, (SELECT location FROM users WHERE username = :curUsername)) / 1000) <= :distance " +
                "ORDER BY distance",
        countQuery = "SELECT count(*) FROM users u " +
                     "WHERE u.role = 'USER' AND u.username != :curUsername " +
                     "AND (ST_DistanceSphere(u.location, (SELECT location FROM users WHERE username = :curUsername)) / 1000) <= :distance",
        nativeQuery = true
    )
    Page<User> findUsersWithinDistance(@Param("curUsername") String curUsername, @Param("distance") double distance, Pageable pageable);    
    
    @Query(
        value = "SELECT u.*, (ST_DistanceSphere(u.location, (SELECT location FROM users WHERE username = :curUsername)) / 1000) as distance " +
                "FROM users u " +
                "WHERE u.role = 'USER' AND u.username != :curUsername " +
                "ORDER BY distance",
        countQuery = "SELECT count(*) FROM users u " +
                     "WHERE u.role = 'USER' AND u.username != :curUsername",
        nativeQuery = true
    )
    Page<User> findAllUsersSortedByProximity(@Param("curUsername") String curUsername, Pageable pageable);
}
