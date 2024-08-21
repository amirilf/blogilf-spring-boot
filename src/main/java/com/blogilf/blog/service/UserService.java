package com.blogilf.blog.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogilf.blog.config.SecurityConfig;
import com.blogilf.blog.exception.CustomBadRequestException;
import com.blogilf.blog.exception.CustomResourceNotFoundException;
import com.blogilf.blog.exception.CustomUnauthorizedException;
import com.blogilf.blog.model.entity.Role;
import com.blogilf.blog.model.entity.User;
import com.blogilf.blog.model.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(SecurityConfig.encoderStrength);

    UserService(UserRepository userRepository, JwtService jwtService){
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUser(String username){
        Optional<User> user = userRepository.findByUsername(username);
        
        if (user.isPresent()) {
            return user.get();
        }
        throw new CustomResourceNotFoundException("User with `" + username + "` username not found!");
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
}
