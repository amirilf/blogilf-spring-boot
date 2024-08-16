package com.blogilf.blog.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogilf.blog.exception.CustomResourceNotFoundException;
import com.blogilf.blog.model.User;
import com.blogilf.blog.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(5);

    UserService(UserRepository userRepository,AuthenticationManager authenticationManager,JwtService jwtService){
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
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
        throw new CustomResourceNotFoundException(username + " not found!");
    }

    public User register(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }
    
    public ResponseEntity<String> verify(User user,HttpServletResponse response){

        // validate username,password and return JWT token

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(user.getUsername());
            System.out.println(token);
            return ResponseEntity.ok(token);
        }
        
        // gonna be handled by AuthenticationException in authenticate method!
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed.");
    }

    @PostConstruct
    public void init(){

        User user1 = User.builder().username("amir").name("Amir").password(encoder.encode("123")).build();
        User user2 = User.builder().username("ali").name("Ali").password(encoder.encode("111")).build();

        userRepository.save(user1);
        userRepository.save(user2);
    }
}
