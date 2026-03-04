package com.school.schoolwebsite.dto;

import com.school.schoolwebsite.entity.SchoolClass;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class AdminStudentEditForm {

    @NotBlank
    private String fullName;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate joiningDate;

    @NotNull
    private SchoolClass schoolClass;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal feePerMonth;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal feeRemaining;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal busFeePerMonth;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal busFeeYearly;

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

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public BigDecimal getFeePerMonth() {
        return feePerMonth;
    }

    public void setFeePerMonth(BigDecimal feePerMonth) {
        this.feePerMonth = feePerMonth;
    }

    public BigDecimal getFeeRemaining() {
        return feeRemaining;
    }

    public void setFeeRemaining(BigDecimal feeRemaining) {
        this.feeRemaining = feeRemaining;
    }

    public BigDecimal getBusFeePerMonth() {
        return busFeePerMonth;
    }

    public void setBusFeePerMonth(BigDecimal busFeePerMonth) {
        this.busFeePerMonth = busFeePerMonth;
    }

    public BigDecimal getBusFeeYearly() {
        return busFeeYearly;
    }

    public void setBusFeeYearly(BigDecimal busFeeYearly) {
        this.busFeeYearly = busFeeYearly;
    }
}
