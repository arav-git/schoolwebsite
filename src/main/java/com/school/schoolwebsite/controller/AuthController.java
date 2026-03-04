package com.school.schoolwebsite.controller;

import com.school.schoolwebsite.dto.RegistrationRequest;
import com.school.schoolwebsite.entity.Role;
import com.school.schoolwebsite.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("request", new RegistrationRequest());
        model.addAttribute("roles", new Role[]{Role.STUDENT, Role.TEACHER});
        return "register";
    }

    @PostMapping("/register")
    public String register(
        @Valid @ModelAttribute("request") RegistrationRequest request,
        BindingResult bindingResult,
        Model model
    ) {
        model.addAttribute("roles", new Role[]{Role.STUDENT, Role.TEACHER});

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.register(request);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("registrationError", ex.getMessage());
            return "register";
        }

        return "redirect:/login?registered";
    }
}
