package com.school.schoolwebsite.repository;

import com.school.schoolwebsite.entity.ClassFeeStructure;
import com.school.schoolwebsite.entity.SchoolClass;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassFeeStructureRepository extends JpaRepository<ClassFeeStructure, Long> {

    Optional<ClassFeeStructure> findBySchoolClass(SchoolClass schoolClass);

    boolean existsBySchoolClass(SchoolClass schoolClass);
}
