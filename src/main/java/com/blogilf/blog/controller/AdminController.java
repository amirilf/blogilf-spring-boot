package com.blogilf.blog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogilf.blog.service.AdminService;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    AdminController(AdminService adminService){
        this.adminService = adminService;
    }
    
    @GetMapping("")
    public String getMethodName() {
        return adminService.AdminPanel();
    }

}
