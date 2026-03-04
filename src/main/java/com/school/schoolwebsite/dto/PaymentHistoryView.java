package com.school.schoolwebsite.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentHistoryView(LocalDate paidOn, BigDecimal amountPaid, String note) {
}
