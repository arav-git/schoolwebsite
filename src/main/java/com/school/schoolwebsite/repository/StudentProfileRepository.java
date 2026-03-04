package com.school.schoolwebsite.repository;

import com.school.schoolwebsite.entity.StudentProfile;
import com.school.schoolwebsite.entity.SchoolClass;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    Optional<StudentProfile> findByUserEmail(String email);
    Optional<StudentProfile> findByUserId(Long userId);

    List<StudentProfile> findBySchoolClassOrderByUserFullNameAsc(SchoolClass schoolClass);

    List<StudentProfile> findAllByOrderByIdDesc();
}
