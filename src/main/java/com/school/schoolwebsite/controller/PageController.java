package com.school.schoolwebsite.controller;

import com.school.schoolwebsite.service.PortalService;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final PortalService portalService;

    public PageController(PortalService portalService) {
        this.portalService = portalService;
    }

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
    public String adminDashboard(Model model) {
        model.addAttribute("students", portalService.getAllStudentsForAdmin());
        model.addAttribute("teachers", portalService.getAllTeachersForAdmin());
        return "admin/dashboard";
    }

    @GetMapping("/teacher/dashboard")
    public String teacherDashboard(Model model, Principal principal) {
        model.addAttribute("teacher", portalService.getTeacherDashboard(principal.getName()));
        return "teacher/dashboard";
    }

    @GetMapping("/student/dashboard")
    public String studentDashboard(Model model, Principal principal) {
        model.addAttribute("student", portalService.getStudentDashboard(principal.getName()));
        return "student/dashboard";
    }
}
