package com.school.schoolwebsite.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "class_fee_structures")
public class ClassFeeStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private SchoolClass schoolClass;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal feePerMonth;

    @Column(precision = 12, scale = 2)
    private BigDecimal busFeePerMonth;

    @Column(precision = 12, scale = 2)
    private BigDecimal busFeeYearly;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
