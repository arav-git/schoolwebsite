package com.school.schoolwebsite.repository;

import com.school.schoolwebsite.entity.StudentAnnualResult;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentAnnualResultRepository extends JpaRepository<StudentAnnualResult, Long> {

    Optional<StudentAnnualResult> findByStudentProfileIdAndAcademicYear(Long studentProfileId, String academicYear);

    List<StudentAnnualResult> findByAcademicYear(String academicYear);
}
