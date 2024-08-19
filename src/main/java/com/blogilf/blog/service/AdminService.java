package com.blogilf.blog.service;

import org.springframework.stereotype.Service;

@Service
public class AdminService {
    
    public String AdminPanel() {
        return "Hey, this is Admin panel.";
    }

}
