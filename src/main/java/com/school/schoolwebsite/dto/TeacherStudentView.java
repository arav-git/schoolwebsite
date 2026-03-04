package com.school.schoolwebsite.dto;

import com.school.schoolwebsite.entity.SchoolClass;
import java.time.LocalDate;

public record TeacherStudentView(
    Long userId,
    String fullName,
    String email,
    LocalDate joiningDate,
    SchoolClass schoolClass
) {
}
