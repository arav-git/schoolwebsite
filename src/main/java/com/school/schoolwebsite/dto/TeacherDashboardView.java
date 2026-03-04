package com.school.schoolwebsite.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record TeacherDashboardView(
    String fullName,
    String email,
    LocalDate joiningDate,
    BigDecimal salaryPerMonth,
    BigDecimal salaryPaid,
    List<PaymentHistoryView> lastSalaryPayments
) {
}
