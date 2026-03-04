package com.school.schoolwebsite.service;

import com.school.schoolwebsite.dto.RegistrationRequest;
import com.school.schoolwebsite.entity.AppUser;
import com.school.schoolwebsite.entity.ClassFeeStructure;
import com.school.schoolwebsite.entity.Role;
import com.school.schoolwebsite.entity.StudentProfile;
import com.school.schoolwebsite.entity.TeacherProfile;
import com.school.schoolwebsite.repository.ClassFeeStructureRepository;
import com.school.schoolwebsite.repository.StudentProfileRepository;
import com.school.schoolwebsite.repository.TeacherProfileRepository;
import com.school.schoolwebsite.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final ClassFeeStructureRepository classFeeStructureRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserRepository userRepository,
        StudentProfileRepository studentProfileRepository,
        TeacherProfileRepository teacherProfileRepository,
        ClassFeeStructureRepository classFeeStructureRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.teacherProfileRepository = teacherProfileRepository;
        this.classFeeStructureRepository = classFeeStructureRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegistrationRequest request) {
        if (request.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("Admin registration is not allowed from this form");
        }

        if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            throw new IllegalArgumentException("Email already registered");
        }

        AppUser user = new AppUser();
        user.setFullName(request.getFullName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setJoiningDate(LocalDate.now());
        userRepository.save(user);

        if (request.getRole() == Role.STUDENT) {
            if (request.getSchoolClass() == null) {
                throw new IllegalArgumentException("School class is required for students");
            }

            ClassFeeStructure feeStructure = classFeeStructureRepository.findBySchoolClass(request.getSchoolClass())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Fee structure is not configured for class " + request.getSchoolClass().name()));

            StudentProfile studentProfile = new StudentProfile();
            studentProfile.setUser(user);
            studentProfile.setSchoolClass(request.getSchoolClass());
            studentProfile.setFeePerMonth(feeStructure.getFeePerMonth());
            studentProfile.setBusFeePerMonth(feeStructure.getBusFeePerMonth());
            studentProfile.setBusFeeYearly(feeStructure.getBusFeeYearly());
            BigDecimal monthlyBus = feeStructure.getBusFeePerMonth() == null
                ? BigDecimal.ZERO
                : feeStructure.getBusFeePerMonth();
            studentProfile.setFeeRemaining(feeStructure.getFeePerMonth().add(monthlyBus));
            studentProfileRepository.save(studentProfile);
            return;
        }

        TeacherProfile teacherProfile = new TeacherProfile();
        teacherProfile.setUser(user);
        teacherProfile.setSalaryPerMonth(BigDecimal.ZERO);
        teacherProfile.setSalaryPaid(BigDecimal.ZERO);
        teacherProfileRepository.save(teacherProfile);
    }
}
