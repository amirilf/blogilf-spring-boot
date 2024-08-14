package com.blogilf.blog.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogilf.blog.exception.CustomResourceNotFoundException;
import com.blogilf.blog.model.User;
import com.blogilf.blog.repository.UserRepository;

import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User register(User user){
        userRepository.save(user);
        return user;
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


    @PostConstruct
    public void init(){

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user1 = User.builder().username("amir").name("Amir").password(encoder.encode("123")).build();
        User user2 = User.builder().username("ali").name("Ali").password(encoder.encode("111")).build();

        userRepository.save(user1);
        userRepository.save(user2);
    }
}
