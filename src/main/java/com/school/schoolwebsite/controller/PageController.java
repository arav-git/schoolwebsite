package com.school.schoolwebsite.controller;

import com.school.schoolwebsite.entity.SchoolClass;
import com.school.schoolwebsite.service.PortalService;
import java.security.Principal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/portal")
    public String portal(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        }
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"))) {
            return "redirect:/teacher/dashboard";
        }
        return "redirect:/student/dashboard";
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

    @GetMapping("/teacher/students")
    public String teacherStudents(
        @RequestParam(name = "schoolClass", required = false) SchoolClass schoolClass,
        Model model
    ) {
        SchoolClass selectedClass = schoolClass == null ? SchoolClass.NURSERY : schoolClass;
        return "redirect:/teacher/students/manage?schoolClass=" + selectedClass;
    }

    @GetMapping("/student/dashboard")
    public String studentDashboard(Model model, Principal principal) {
        model.addAttribute("student", portalService.getStudentDashboard(principal.getName()));
        return "student/dashboard";
    }
}
