package com.school.schoolwebsite.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "student_class_promotions")
public class StudentClassPromotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_profile_id", nullable = false)
    private StudentProfile studentProfile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SchoolClass fromClass;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SchoolClass toClass;

    @Column(nullable = false)
    private String academicYear;

    @Column(nullable = false)
    private LocalDate promotedOn;

    @Column(nullable = false)
    private String promotedByEmail;

    @Column(length = 300)
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentProfile getStudentProfile() {
        return studentProfile;
    }

    public void setStudentProfile(StudentProfile studentProfile) {
        this.studentProfile = studentProfile;
    }

    public SchoolClass getFromClass() {
        return fromClass;
    }

    public void setFromClass(SchoolClass fromClass) {
        this.fromClass = fromClass;
    }

    public SchoolClass getToClass() {
        return toClass;
    }

    public void setToClass(SchoolClass toClass) {
        this.toClass = toClass;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public LocalDate getPromotedOn() {
        return promotedOn;
    }

    public void setPromotedOn(LocalDate promotedOn) {
        this.promotedOn = promotedOn;
    }

    public String getPromotedByEmail() {
        return promotedByEmail;
    }

    public void setPromotedByEmail(String promotedByEmail) {
        this.promotedByEmail = promotedByEmail;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
