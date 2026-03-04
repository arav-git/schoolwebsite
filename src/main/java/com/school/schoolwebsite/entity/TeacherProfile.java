package com.school.schoolwebsite.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "teacher_profiles")
public class TeacherProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private AppUser user;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salaryPerMonth;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salaryPaid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
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
