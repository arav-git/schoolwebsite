package com.school.schoolwebsite.repository;

import com.school.schoolwebsite.entity.TeacherProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {

    Optional<TeacherProfile> findByUserEmail(String email);
    Optional<TeacherProfile> findByUserId(Long userId);

    List<TeacherProfile> findAllByOrderByIdDesc();
}
