package com.blogilf.blog.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogilf.blog.model.entity.User;
import com.blogilf.blog.model.projection.UserDistanceProjection;
import com.blogilf.blog.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }
    
    @GetMapping("")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/nearby")
    public Page<User> getNearbyUsers(
        @RequestParam(required = false) @Positive Double distance,  // distance in km
        @RequestParam(defaultValue = "1") @Min(1) int page) {
        
        return userService.getNearbyUsers(distance, page);
    }
    
    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @GetMapping("/{username}/dist")
    public UserDistanceProjection getUserDistance(@PathVariable String username) {
        return userService.getUserDistance(username);
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody User user){
        return userService.register(user); 
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user, HttpServletResponse response) {
        return userService.login(user,response);
    }
    
}