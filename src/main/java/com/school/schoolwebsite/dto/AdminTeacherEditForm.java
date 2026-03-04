package com.school.schoolwebsite.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class AdminTeacherEditForm {

    @NotBlank
    private String fullName;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate joiningDate;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal salaryPerMonth;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal salaryPaid;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public BigDecimal getSalaryPerMonth() {
        return salaryPerMonth;
    }

    public void setSalaryPerMonth(BigDecimal salaryPerMonth) {
        this.salaryPerMonth = salaryPerMonth;
    }

    public BigDecimal getSalaryPaid() {
        return salaryPaid;
    }

    public void setSalaryPaid(BigDecimal salaryPaid) {
        this.salaryPaid = salaryPaid;
    }
}
