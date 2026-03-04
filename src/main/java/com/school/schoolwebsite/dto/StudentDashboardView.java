package com.school.schoolwebsite.dto;

import com.school.schoolwebsite.entity.SchoolClass;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record StudentDashboardView(
    String fullName,
    String email,
    LocalDate joiningDate,
    SchoolClass schoolClass,
    BigDecimal feePerMonth,
    BigDecimal feeRemaining,
    BigDecimal busFeePerMonth,
    BigDecimal busFeeYearly,
    List<PaymentHistoryView> lastFeePayments
) {
}
