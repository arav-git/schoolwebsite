package com.school.schoolwebsite.repository;

import com.school.schoolwebsite.entity.StudentClassPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentClassPromotionRepository extends JpaRepository<StudentClassPromotion, Long> {

    boolean existsByStudentProfileIdAndAcademicYear(Long studentProfileId, String academicYear);
}
