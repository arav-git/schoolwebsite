package com.school.schoolwebsite.service;

import com.school.schoolwebsite.dto.StudentStatusManageView;
import com.school.schoolwebsite.entity.ExamStatus;
import com.school.schoolwebsite.entity.SchoolClass;
import com.school.schoolwebsite.entity.StudentAnnualResult;
import com.school.schoolwebsite.entity.StudentProfile;
import com.school.schoolwebsite.repository.StudentAnnualResultRepository;
import com.school.schoolwebsite.repository.StudentProfileRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentStatusService {

    private final StudentProfileRepository studentProfileRepository;
    private final StudentAnnualResultRepository studentAnnualResultRepository;

    public StudentStatusService(
        StudentProfileRepository studentProfileRepository,
        StudentAnnualResultRepository studentAnnualResultRepository
    ) {
        this.studentProfileRepository = studentProfileRepository;
        this.studentAnnualResultRepository = studentAnnualResultRepository;
    }

    @Transactional(readOnly = true)
    public List<StudentStatusManageView> listByClassAndYear(SchoolClass schoolClass, String academicYear) {
        return studentProfileRepository.findBySchoolClassOrderByUserFullNameAsc(schoolClass).stream()
            .map(profile -> {
                ExamStatus status = studentAnnualResultRepository
                    .findByStudentProfileIdAndAcademicYear(profile.getId(), academicYear)
                    .map(StudentAnnualResult::getExamStatus)
                    .orElse(ExamStatus.PENDING);

                return new StudentStatusManageView(
                    profile.getUser().getId(),
                    profile.getUser().getFullName(),
                    profile.getUser().getEmail(),
                    profile.getUser().getJoiningDate(),
                    profile.getSchoolClass(),
                    status
                );
            })
            .toList();
    }

    @Transactional
    public void updateExamStatus(Long userId, String academicYear, ExamStatus examStatus, String updatedByEmail) {
        StudentProfile profile = studentProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        StudentAnnualResult result = studentAnnualResultRepository
            .findByStudentProfileIdAndAcademicYear(profile.getId(), academicYear)
            .orElseGet(StudentAnnualResult::new);

        result.setStudentProfile(profile);
        result.setAcademicYear(academicYear);
        result.setExamStatus(examStatus);
        result.setUpdatedOn(LocalDate.now());
        result.setUpdatedByEmail(updatedByEmail);
        studentAnnualResultRepository.save(result);
    }
}
