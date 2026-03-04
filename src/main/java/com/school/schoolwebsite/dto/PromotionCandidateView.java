package com.school.schoolwebsite.dto;

import com.school.schoolwebsite.entity.SchoolClass;
import com.school.schoolwebsite.entity.ExamStatus;

public record PromotionCandidateView(
    Long userId,
    String fullName,
    String email,
    SchoolClass currentClass,
    SchoolClass nextClass,
    ExamStatus examStatus
) {
}
