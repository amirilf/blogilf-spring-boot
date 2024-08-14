package com.blogilf.blog.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogilf.blog.model.User;
import com.blogilf.blog.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userService.register(user); 
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        return userService.verify(user);
    }
    
}