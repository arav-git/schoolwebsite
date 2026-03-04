package com.school.schoolwebsite.controller;

import com.school.schoolwebsite.dto.AdminStudentEditForm;
import com.school.schoolwebsite.entity.ClassFeeStructure;
import com.school.schoolwebsite.entity.ExamStatus;
import com.school.schoolwebsite.entity.SchoolClass;
import com.school.schoolwebsite.repository.ClassFeeStructureRepository;
import com.school.schoolwebsite.service.AdminManagementService;
import com.school.schoolwebsite.service.PromotionService;
import com.school.schoolwebsite.service.StudentStatusService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.LinkedHashMap;
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
public class StudentManagementController {

    private final StudentStatusService studentStatusService;
    private final PromotionService promotionService;
    private final AdminManagementService adminManagementService;
    private final ClassFeeStructureRepository classFeeStructureRepository;

    public StudentManagementController(
        StudentStatusService studentStatusService,
        PromotionService promotionService,
        AdminManagementService adminManagementService,
        ClassFeeStructureRepository classFeeStructureRepository
    ) {
        this.studentStatusService = studentStatusService;
        this.promotionService = promotionService;
        this.adminManagementService = adminManagementService;
        this.classFeeStructureRepository = classFeeStructureRepository;
    }

    @GetMapping("/admin/students/manage")
    public String adminStudentManage(
        @RequestParam(name = "schoolClass", required = false) SchoolClass schoolClass,
        @RequestParam(name = "academicYear", required = false) String academicYear,
        Model model
    ) {
        return managePage("ADMIN", "/admin/students/manage", schoolClass, academicYear, model);
    }

    @GetMapping("/teacher/students/manage")
    public String teacherStudentManage(
        @RequestParam(name = "schoolClass", required = false) SchoolClass schoolClass,
        @RequestParam(name = "academicYear", required = false) String academicYear,
        Model model
    ) {
        return managePage("TEACHER", "/teacher/students/manage", schoolClass, academicYear, model);
    }

    @PostMapping("/admin/students/{userId}/status")
    public String adminUpdateStudentStatus(
        @PathVariable Long userId,
        @RequestParam("academicYear") String academicYear,
        @RequestParam("schoolClass") SchoolClass schoolClass,
        @RequestParam("examStatus") ExamStatus examStatus,
        Principal principal
    ) {
        studentStatusService.updateExamStatus(userId, academicYear, examStatus, principal.getName());
        return "redirect:/admin/students/manage?schoolClass=" + schoolClass + "&academicYear=" + academicYear + "&statusUpdated";
    }

    @PostMapping("/teacher/students/{userId}/status")
    public String teacherUpdateStudentStatus(
        @PathVariable Long userId,
        @RequestParam("academicYear") String academicYear,
        @RequestParam("schoolClass") SchoolClass schoolClass,
        @RequestParam("examStatus") ExamStatus examStatus,
        Principal principal
    ) {
        studentStatusService.updateExamStatus(userId, academicYear, examStatus, principal.getName());
        return "redirect:/teacher/students/manage?schoolClass=" + schoolClass + "&academicYear=" + academicYear + "&statusUpdated";
    }

    @GetMapping("/teacher/students/{userId}/edit")
    public String editStudentByTeacher(@PathVariable Long userId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("classes", SchoolClass.values());
        model.addAttribute("classFeeMap", buildClassFeeMap());
        model.addAttribute("form", adminManagementService.getStudentEditForm(userId));
        model.addAttribute("backRoute", "/teacher/students/manage");
        model.addAttribute("submitRoute", "/teacher/students/" + userId + "/edit");
        return "teacher/edit-student";
    }

    @PostMapping("/teacher/students/{userId}/edit")
    public String updateStudentByTeacher(
        @PathVariable Long userId,
        @Valid @ModelAttribute("form") AdminStudentEditForm form,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", userId);
            model.addAttribute("classes", SchoolClass.values());
            model.addAttribute("classFeeMap", buildClassFeeMap());
            model.addAttribute("backRoute", "/teacher/students/manage");
            model.addAttribute("submitRoute", "/teacher/students/" + userId + "/edit");
            return "teacher/edit-student";
        }

        adminManagementService.updateStudent(userId, form);
        return "redirect:/teacher/students/manage?studentUpdated";
    }

    private String managePage(
        String viewRole,
        String actionBase,
        SchoolClass schoolClass,
        String academicYear,
        Model model
    ) {
        SchoolClass selectedClass = schoolClass == null ? SchoolClass.NURSERY : schoolClass;
        String selectedYear = (academicYear == null || academicYear.isBlank())
            ? promotionService.defaultAcademicYear()
            : academicYear;

        model.addAttribute("viewRole", viewRole);
        model.addAttribute("actionBase", actionBase);
        model.addAttribute("classes", SchoolClass.values());
        model.addAttribute("examStatuses", ExamStatus.values());
        model.addAttribute("selectedClass", selectedClass);
        model.addAttribute("academicYear", selectedYear);
        model.addAttribute("students", studentStatusService.listByClassAndYear(selectedClass, selectedYear));
        return "teacher/students-manage";
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
