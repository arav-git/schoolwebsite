package com.school.schoolwebsite.service;

import com.school.schoolwebsite.dto.PromotionCandidateView;
import com.school.schoolwebsite.entity.ExamStatus;
import com.school.schoolwebsite.entity.ClassFeeStructure;
import com.school.schoolwebsite.entity.SchoolClass;
import com.school.schoolwebsite.entity.StudentAnnualResult;
import com.school.schoolwebsite.entity.StudentClassPromotion;
import com.school.schoolwebsite.entity.StudentProfile;
import com.school.schoolwebsite.repository.ClassFeeStructureRepository;
import com.school.schoolwebsite.repository.StudentAnnualResultRepository;
import com.school.schoolwebsite.repository.StudentClassPromotionRepository;
import com.school.schoolwebsite.repository.StudentProfileRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PromotionService {

    private final StudentProfileRepository studentProfileRepository;
    private final ClassFeeStructureRepository classFeeStructureRepository;
    private final StudentClassPromotionRepository studentClassPromotionRepository;
    private final StudentAnnualResultRepository studentAnnualResultRepository;

    public PromotionService(
        StudentProfileRepository studentProfileRepository,
        ClassFeeStructureRepository classFeeStructureRepository,
        StudentClassPromotionRepository studentClassPromotionRepository,
        StudentAnnualResultRepository studentAnnualResultRepository
    ) {
        this.studentProfileRepository = studentProfileRepository;
        this.classFeeStructureRepository = classFeeStructureRepository;
        this.studentClassPromotionRepository = studentClassPromotionRepository;
        this.studentAnnualResultRepository = studentAnnualResultRepository;
    }

    @Transactional(readOnly = true)
    public List<PromotionCandidateView> getPromotionCandidates(String academicYear) {
        return studentProfileRepository.findAllByOrderByIdDesc().stream()
            .filter(profile -> nextClass(profile.getSchoolClass()) != null)
            .map(profile -> {
                ExamStatus status = studentAnnualResultRepository
                    .findByStudentProfileIdAndAcademicYear(profile.getId(), academicYear)
                    .map(StudentAnnualResult::getExamStatus)
                    .orElse(ExamStatus.PENDING);
                return new PromotionCandidateView(
                    profile.getUser().getId(),
                    profile.getUser().getFullName(),
                    profile.getUser().getEmail(),
                    profile.getSchoolClass(),
                    nextClass(profile.getSchoolClass()),
                    status
                );
            })
            .filter(candidate -> candidate.examStatus() == ExamStatus.PASS)
            .toList();
    }

    @Transactional
    public int autoPromoteEligible(String academicYear) {
        List<Long> userIds = getPromotionCandidates(academicYear).stream()
            .map(PromotionCandidateView::userId)
            .toList();
        return bulkPromote(userIds, academicYear, "SYSTEM_AUTO");
    }

    @Transactional
    public int bulkPromote(List<Long> studentUserIds, String academicYear, String promotedByEmail) {
        if (studentUserIds == null || studentUserIds.isEmpty()) {
            return 0;
        }

        int promotedCount = 0;
        for (Long userId : studentUserIds) {
            StudentProfile profile = studentProfileRepository.findByUserId(userId).orElse(null);
            if (profile == null) {
                continue;
            }

            ExamStatus status = studentAnnualResultRepository
                .findByStudentProfileIdAndAcademicYear(profile.getId(), academicYear)
                .map(StudentAnnualResult::getExamStatus)
                .orElse(ExamStatus.PENDING);

            if (status != ExamStatus.PASS) {
                continue;
            }

            SchoolClass nextClass = nextClass(profile.getSchoolClass());
            if (nextClass == null) {
                continue;
            }

            if (studentClassPromotionRepository.existsByStudentProfileIdAndAcademicYear(profile.getId(), academicYear)) {
                continue;
            }

            ClassFeeStructure nextFee = classFeeStructureRepository.findBySchoolClass(nextClass)
                .orElseThrow(() -> new IllegalArgumentException("Fee structure missing for class " + nextClass));

            BigDecimal previousRemaining = profile.getFeeRemaining() == null ? BigDecimal.ZERO : profile.getFeeRemaining();
            BigDecimal busPerMonth = nextFee.getBusFeePerMonth() == null ? BigDecimal.ZERO : nextFee.getBusFeePerMonth();
            BigDecimal newCurrentMonthDue = Objects.requireNonNull(nextFee.getFeePerMonth()).add(busPerMonth);

            SchoolClass fromClass = profile.getSchoolClass();
            profile.setSchoolClass(nextClass);
            profile.setFeePerMonth(nextFee.getFeePerMonth());
            profile.setBusFeePerMonth(nextFee.getBusFeePerMonth());
            profile.setBusFeeYearly(nextFee.getBusFeeYearly());
            profile.setFeeRemaining(previousRemaining.add(newCurrentMonthDue));

            StudentClassPromotion promotion = new StudentClassPromotion();
            promotion.setStudentProfile(profile);
            promotion.setFromClass(fromClass);
            promotion.setToClass(nextClass);
            promotion.setAcademicYear(academicYear);
            promotion.setPromotedOn(LocalDate.now());
            promotion.setPromotedByEmail(promotedByEmail);
            promotion.setNote("Bulk promotion");
            studentClassPromotionRepository.save(promotion);
            promotedCount++;
        }

        return promotedCount;
    }

    @Transactional(readOnly = true)
    public List<PromotionCandidateView> getAllPromotionCandidatesWithStatus(String academicYear) {
        return studentProfileRepository.findAllByOrderByIdDesc().stream()
            .filter(profile -> nextClass(profile.getSchoolClass()) != null)
            .map(profile -> new PromotionCandidateView(
                profile.getUser().getId(),
                profile.getUser().getFullName(),
                profile.getUser().getEmail(),
                profile.getSchoolClass(),
                nextClass(profile.getSchoolClass()),
                studentAnnualResultRepository.findByStudentProfileIdAndAcademicYear(profile.getId(), academicYear)
                    .map(StudentAnnualResult::getExamStatus)
                    .orElse(ExamStatus.PENDING)
            ))
            .toList();
    }

    public String defaultAcademicYear() {
        int year = LocalDate.now().getYear();
        return year + "-" + (year + 1);
    }

    private SchoolClass nextClass(SchoolClass currentClass) {
        List<SchoolClass> values = new ArrayList<>(List.of(SchoolClass.values()));
        int currentIndex = values.indexOf(currentClass);
        if (currentIndex < 0 || currentIndex + 1 >= values.size()) {
            return null;
        }
        return values.get(currentIndex + 1);
    }
}
