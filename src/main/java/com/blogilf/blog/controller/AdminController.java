package com.blogilf.blog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogilf.blog.model.entity.Article;
import com.blogilf.blog.service.AdminService;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.time.LocalDate;


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

    @GetMapping("/articles")
    public List<Article> getFilteredArticles(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String slug,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) LocalDate dateBegin,
            @RequestParam(required = false) LocalDate dateEnd,
            @RequestParam(required = false) Long vmin,
            @RequestParam(required = false) Long vmax,
            @RequestParam(required = false) Integer rtmax,
            @RequestParam(required = false) Integer rtmin) {

        return adminService.getFilteredArticles(
                title, slug, content, dateBegin, dateEnd,
                vmin, vmax, rtmin, rtmax);
    }

}
