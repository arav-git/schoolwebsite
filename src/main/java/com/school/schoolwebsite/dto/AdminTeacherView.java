package com.school.schoolwebsite.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AdminTeacherView(
    Long userId,
    String fullName,
    String email,
    LocalDate joiningDate,
    BigDecimal salaryPerMonth,
    BigDecimal salaryPaid
) {
}
