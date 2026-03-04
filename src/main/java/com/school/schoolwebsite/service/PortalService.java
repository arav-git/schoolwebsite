package com.school.schoolwebsite.service;

import com.school.schoolwebsite.dto.AdminStudentView;
import com.school.schoolwebsite.dto.AdminTeacherView;
import com.school.schoolwebsite.dto.PaymentHistoryView;
import com.school.schoolwebsite.dto.StudentDashboardView;
import com.school.schoolwebsite.dto.TeacherStudentView;
import com.school.schoolwebsite.dto.TeacherDashboardView;
import com.school.schoolwebsite.entity.SchoolClass;
import com.school.schoolwebsite.entity.StudentProfile;
import com.school.schoolwebsite.entity.TeacherProfile;
import com.school.schoolwebsite.repository.StudentFeePaymentRepository;
import com.school.schoolwebsite.repository.StudentProfileRepository;
import com.school.schoolwebsite.repository.TeacherProfileRepository;
import com.school.schoolwebsite.repository.TeacherSalaryPaymentRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PortalService {

    private final StudentProfileRepository studentProfileRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final StudentFeePaymentRepository studentFeePaymentRepository;
    private final TeacherSalaryPaymentRepository teacherSalaryPaymentRepository;

    public PortalService(
        StudentProfileRepository studentProfileRepository,
        TeacherProfileRepository teacherProfileRepository,
        StudentFeePaymentRepository studentFeePaymentRepository,
        TeacherSalaryPaymentRepository teacherSalaryPaymentRepository
    ) {
        this.studentProfileRepository = studentProfileRepository;
        this.teacherProfileRepository = teacherProfileRepository;
        this.studentFeePaymentRepository = studentFeePaymentRepository;
        this.teacherSalaryPaymentRepository = teacherSalaryPaymentRepository;
    }

    @Transactional(readOnly = true)
    public StudentDashboardView getStudentDashboard(String email) {
        StudentProfile profile = studentProfileRepository.findByUserEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Student profile not found"));

        List<PaymentHistoryView> feeHistory = studentFeePaymentRepository
            .findTop10ByStudentProfileIdOrderByPaidOnDesc(profile.getId())
            .stream()
            .map(payment -> new PaymentHistoryView(payment.getPaidOn(), payment.getAmountPaid(), payment.getNote()))
            .toList();

        return new StudentDashboardView(
            profile.getUser().getFullName(),
            profile.getUser().getEmail(),
            profile.getUser().getJoiningDate(),
            profile.getSchoolClass(),
            profile.getFeePerMonth(),
            profile.getFeeRemaining(),
            profile.getBusFeePerMonth(),
            profile.getBusFeeYearly(),
            feeHistory
        );
    }

    @Transactional(readOnly = true)
    public TeacherDashboardView getTeacherDashboard(String email) {
        TeacherProfile profile = teacherProfileRepository.findByUserEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Teacher profile not found"));

        List<PaymentHistoryView> salaryHistory = teacherSalaryPaymentRepository
            .findTop10ByTeacherProfileIdOrderByPaidOnDesc(profile.getId())
            .stream()
            .map(payment -> new PaymentHistoryView(payment.getPaidOn(), payment.getAmountPaid(), payment.getNote()))
            .toList();

        return new TeacherDashboardView(
            profile.getUser().getFullName(),
            profile.getUser().getEmail(),
            profile.getUser().getJoiningDate(),
            profile.getSalaryPerMonth(),
            profile.getSalaryPaid(),
            salaryHistory
        );
    }

    @Transactional(readOnly = true)
    public List<AdminStudentView> getAllStudentsForAdmin() {
        return studentProfileRepository.findAllByOrderByIdDesc().stream()
            .map(profile -> new AdminStudentView(
                profile.getUser().getId(),
                profile.getUser().getFullName(),
                profile.getUser().getEmail(),
                profile.getUser().getJoiningDate() == null ? LocalDate.now() : profile.getUser().getJoiningDate(),
                profile.getSchoolClass(),
                profile.getFeePerMonth(),
                profile.getFeeRemaining(),
                profile.getBusFeePerMonth(),
                profile.getBusFeeYearly()
            ))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<AdminTeacherView> getAllTeachersForAdmin() {
        return teacherProfileRepository.findAllByOrderByIdDesc().stream()
            .map(profile -> new AdminTeacherView(
                profile.getUser().getId(),
                profile.getUser().getFullName(),
                profile.getUser().getEmail(),
                profile.getUser().getJoiningDate() == null ? LocalDate.now() : profile.getUser().getJoiningDate(),
                profile.getSalaryPerMonth(),
                profile.getSalaryPaid()
            ))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<TeacherStudentView> getStudentsForTeacherByClass(SchoolClass schoolClass) {
        return studentProfileRepository.findBySchoolClassOrderByUserFullNameAsc(schoolClass).stream()
            .map(profile -> new TeacherStudentView(
                profile.getUser().getId(),
                profile.getUser().getFullName(),
                profile.getUser().getEmail(),
                profile.getUser().getJoiningDate() == null ? LocalDate.now() : profile.getUser().getJoiningDate(),
                profile.getSchoolClass()
            ))
            .toList();
    }
}
