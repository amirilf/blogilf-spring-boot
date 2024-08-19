package com.blogilf.blog.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogilf.blog.config.SecurityConfig;
import com.blogilf.blog.exception.CustomResourceNotFoundException;
import com.blogilf.blog.exception.CustomUnauthorizedException;
import com.blogilf.blog.model.Role;
import com.blogilf.blog.model.User;
import com.blogilf.blog.repository.UserRepository;

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
        throw new CustomResourceNotFoundException("User with username: " + username + " is not found!");
    }

    public User register(User user){

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.USER);    
        
        userRepository.save(user);
        
        return user;
    }
    
    public ResponseEntity<String> login(User user,HttpServletResponse response){

        User dbUser = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new CustomResourceNotFoundException("Username or password is wrong."));

        System.out.println(user.getPassword());
        System.out.println(encoder.encode(user.getPassword()));
        System.out.println(dbUser.getPassword());

        if (!encoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new CustomUnauthorizedException("Username or password is wrong.");
        }

        String token = jwtService.generateToken(dbUser.getUsername(),dbUser.getRole().name());
        return ResponseEntity.ok(token);
    }
}
