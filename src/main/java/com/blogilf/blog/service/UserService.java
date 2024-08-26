package com.blogilf.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogilf.blog.config.SecurityConfig;
import com.blogilf.blog.exception.CustomBadRequestException;
import com.blogilf.blog.exception.CustomResourceNotFoundException;
import com.blogilf.blog.exception.CustomUnauthorizedException;
import com.blogilf.blog.model.entity.Role;
import com.blogilf.blog.model.entity.User;
import com.blogilf.blog.model.projection.UserDistanceProjection;
import com.blogilf.blog.model.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(SecurityConfig.encoderStrength);
    private final int userPageSize = 10;

    UserService(UserRepository userRepository, JwtService jwtService){
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public Page<User> getNearbyUsers(Double distance, int page) {

        String curUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable pageable = PageRequest.of(page-1, userPageSize);

        Page<User> nearbyUsers;
        if (distance != null) {
            nearbyUsers = userRepository.findUsersWithinDistance(curUsername, distance, pageable);
        } else {
            nearbyUsers = userRepository.findAllUsersSortedByProximity(curUsername, pageable);
        }
        
        if (nearbyUsers.isEmpty()) {
            throw new CustomResourceNotFoundException("No users found.");
        }

        return nearbyUsers;
    }
    
    public User getUser(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(() -> new CustomResourceNotFoundException("User with `" + username + "` username not found!"));
    }

    public User register(User user){

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new CustomBadRequestException("Username already taken.");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.USER);    
        
        userRepository.save(user);
        
        return user;
    }
    
    public ResponseEntity<String> login(User user,HttpServletResponse response){

        User dbUser = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new CustomUnauthorizedException("Username or password is incorrect."));

        if (!encoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new CustomUnauthorizedException("Username or password is incorrect.");
        }

        String token = jwtService.generateToken(dbUser.getUsername(),dbUser.getRole().name());
        return ResponseEntity.ok(token);
    }

    public UserDistanceProjection getUserDistance(String username) {
        
        if (!userRepository.existsByUsername(username)) {
            throw new CustomResourceNotFoundException("User with `" + username + "` username not found!");
        }

        String curUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        UserDistanceProjection projection = userRepository.findDistanceAndLocations(curUsername, username);

        if (projection == null) {
            throw new IllegalStateException("Could not find distance or location data.");
        }

        return projection;
    }
}
