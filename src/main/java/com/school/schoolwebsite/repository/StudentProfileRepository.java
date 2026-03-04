package com.school.schoolwebsite.repository;

import com.school.schoolwebsite.entity.StudentProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    Optional<StudentProfile> findByUserEmail(String email);

    List<StudentProfile> findAllByOrderByIdDesc();
}
