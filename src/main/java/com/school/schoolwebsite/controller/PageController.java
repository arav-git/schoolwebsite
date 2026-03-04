package com.school.schoolwebsite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/teacher/dashboard")
    public String teacherDashboard() {
        return "teacher/dashboard";
    }

    @GetMapping("/student/dashboard")
    public String studentDashboard() {
        return "student/dashboard";
    }
}
