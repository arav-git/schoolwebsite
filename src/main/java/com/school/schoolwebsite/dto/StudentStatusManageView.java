package com.school.schoolwebsite.dto;

import com.school.schoolwebsite.entity.ExamStatus;
import com.school.schoolwebsite.entity.SchoolClass;
import java.time.LocalDate;

public record StudentStatusManageView(
    Long userId,
    String fullName,
    String email,
    LocalDate joiningDate,
    SchoolClass schoolClass,
    ExamStatus examStatus
) {
}
