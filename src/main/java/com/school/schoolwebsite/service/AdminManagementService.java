package com.school.schoolwebsite.service;

import com.school.schoolwebsite.dto.AdminStudentEditForm;
import com.school.schoolwebsite.dto.AdminTeacherEditForm;
import com.school.schoolwebsite.entity.ClassFeeStructure;
import com.school.schoolwebsite.entity.StudentProfile;
import com.school.schoolwebsite.entity.TeacherProfile;
import com.school.schoolwebsite.repository.ClassFeeStructureRepository;
import com.school.schoolwebsite.repository.StudentProfileRepository;
import com.school.schoolwebsite.repository.TeacherProfileRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminManagementService {

    private final StudentProfileRepository studentProfileRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final ClassFeeStructureRepository classFeeStructureRepository;

    public AdminManagementService(
        StudentProfileRepository studentProfileRepository,
        TeacherProfileRepository teacherProfileRepository,
        ClassFeeStructureRepository classFeeStructureRepository
    ) {
        this.studentProfileRepository = studentProfileRepository;
        this.teacherProfileRepository = teacherProfileRepository;
        this.classFeeStructureRepository = classFeeStructureRepository;
    }

    @Transactional(readOnly = true)
    public AdminStudentEditForm getStudentEditForm(Long userId) {
        StudentProfile profile = studentProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        AdminStudentEditForm form = new AdminStudentEditForm();
        form.setFullName(profile.getUser().getFullName());
        form.setJoiningDate(profile.getUser().getJoiningDate() == null
            ? LocalDate.now()
            : profile.getUser().getJoiningDate());
        form.setSchoolClass(profile.getSchoolClass());
        form.setFeePerMonth(profile.getFeePerMonth());
        form.setFeeRemaining(profile.getFeeRemaining());
        form.setBusFeePerMonth(profile.getBusFeePerMonth());
        form.setBusFeeYearly(profile.getBusFeeYearly());
        return form;
    }

    @Transactional
    public void updateStudent(Long userId, AdminStudentEditForm form) {
        StudentProfile profile = studentProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        profile.getUser().setFullName(form.getFullName().trim());
        profile.getUser().setJoiningDate(form.getJoiningDate() == null ? LocalDate.now() : form.getJoiningDate());

        ClassFeeStructure feeStructure = classFeeStructureRepository.findBySchoolClass(form.getSchoolClass())
            .orElseThrow(() -> new IllegalArgumentException(
                "Fee structure not found for class " + form.getSchoolClass()));

        profile.setSchoolClass(form.getSchoolClass());
        profile.setFeePerMonth(feeStructure.getFeePerMonth());
        profile.setBusFeePerMonth(feeStructure.getBusFeePerMonth());
        profile.setBusFeeYearly(feeStructure.getBusFeeYearly());
        BigDecimal monthlyBus = feeStructure.getBusFeePerMonth() == null
            ? BigDecimal.ZERO
            : feeStructure.getBusFeePerMonth();
        profile.setFeeRemaining(feeStructure.getFeePerMonth().add(monthlyBus));
    }

    @Transactional(readOnly = true)
    public AdminTeacherEditForm getTeacherEditForm(Long userId) {
        TeacherProfile profile = teacherProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        AdminTeacherEditForm form = new AdminTeacherEditForm();
        form.setFullName(profile.getUser().getFullName());
        form.setJoiningDate(profile.getUser().getJoiningDate() == null
            ? LocalDate.now()
            : profile.getUser().getJoiningDate());
        form.setSalaryPerMonth(profile.getSalaryPerMonth());
        form.setSalaryPaid(profile.getSalaryPaid());
        return form;
    }

    @Transactional
    public void updateTeacher(Long userId, AdminTeacherEditForm form) {
        TeacherProfile profile = teacherProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        profile.getUser().setFullName(form.getFullName().trim());
        profile.getUser().setJoiningDate(form.getJoiningDate() == null ? LocalDate.now() : form.getJoiningDate());
        profile.setSalaryPerMonth(form.getSalaryPerMonth());
        profile.setSalaryPaid(form.getSalaryPaid());
    }
}
