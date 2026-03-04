package com.school.schoolwebsite.controller;

import com.school.schoolwebsite.dto.AdminStudentEditForm;
import com.school.schoolwebsite.dto.AdminTeacherEditForm;
import com.school.schoolwebsite.entity.ClassFeeStructure;
import com.school.schoolwebsite.entity.SchoolClass;
import com.school.schoolwebsite.repository.ClassFeeStructureRepository;
import com.school.schoolwebsite.service.AdminManagementService;
import com.school.schoolwebsite.service.PromotionService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminManagementController {

    private final AdminManagementService adminManagementService;
    private final ClassFeeStructureRepository classFeeStructureRepository;
    private final PromotionService promotionService;

    public AdminManagementController(
        AdminManagementService adminManagementService,
        ClassFeeStructureRepository classFeeStructureRepository,
        PromotionService promotionService
    ) {
        this.adminManagementService = adminManagementService;
        this.classFeeStructureRepository = classFeeStructureRepository;
        this.promotionService = promotionService;
    }

    @GetMapping("/admin/students/{userId}/edit")
    public String editStudentPage(@PathVariable Long userId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("classes", SchoolClass.values());
        model.addAttribute("classFeeMap", buildClassFeeMap());
        model.addAttribute("form", adminManagementService.getStudentEditForm(userId));
        return "admin/edit-student";
    }

    @PostMapping("/admin/students/{userId}/edit")
    public String updateStudent(
        @PathVariable Long userId,
        @Valid @ModelAttribute("form") AdminStudentEditForm form,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", userId);
            model.addAttribute("classes", SchoolClass.values());
            model.addAttribute("classFeeMap", buildClassFeeMap());
            return "admin/edit-student";
        }

        adminManagementService.updateStudent(userId, form);
        return "redirect:/admin/dashboard?studentUpdated";
    }

    @GetMapping("/admin/teachers/{userId}/edit")
    public String editTeacherPage(@PathVariable Long userId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("form", adminManagementService.getTeacherEditForm(userId));
        return "admin/edit-teacher";
    }

    @PostMapping("/admin/teachers/{userId}/edit")
    public String updateTeacher(
        @PathVariable Long userId,
        @Valid @ModelAttribute("form") AdminTeacherEditForm form,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", userId);
            return "admin/edit-teacher";
        }

        adminManagementService.updateTeacher(userId, form);
        return "redirect:/admin/dashboard?teacherUpdated";
    }

    @GetMapping("/admin/promotions")
    public String promotionPage(
        @RequestParam(name = "academicYear", required = false) String academicYear,
        Model model
    ) {
        String selectedYear = (academicYear == null || academicYear.isBlank())
            ? promotionService.defaultAcademicYear()
            : academicYear;
        model.addAttribute("academicYear", selectedYear);
        model.addAttribute("candidates", promotionService.getAllPromotionCandidatesWithStatus(selectedYear));
        return "admin/promotions";
    }

    @PostMapping("/admin/promotions")
    public String bulkPromote(
        @RequestParam("academicYear") String academicYear,
        @RequestParam(name = "studentUserIds", required = false) List<Long> studentUserIds,
        Principal principal
    ) {
        int promotedCount = promotionService.bulkPromote(studentUserIds, academicYear, principal.getName());
        return "redirect:/admin/promotions?promotedCount=" + promotedCount;
    }

    private Map<String, Map<String, BigDecimal>> buildClassFeeMap() {
        Map<String, Map<String, BigDecimal>> map = new LinkedHashMap<>();
        for (ClassFeeStructure feeStructure : classFeeStructureRepository.findAll()) {
            Map<String, BigDecimal> values = new LinkedHashMap<>();
            values.put("feePerMonth", feeStructure.getFeePerMonth());
            values.put("busFeePerMonth", feeStructure.getBusFeePerMonth());
            values.put("busFeeYearly", feeStructure.getBusFeeYearly());
            map.put(feeStructure.getSchoolClass().name(), values);
        }
        return map;
    }
}
