package com.school.schoolwebsite.repository;

import com.school.schoolwebsite.entity.TeacherSalaryPayment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherSalaryPaymentRepository extends JpaRepository<TeacherSalaryPayment, Long> {

    List<TeacherSalaryPayment> findTop10ByTeacherProfileIdOrderByPaidOnDesc(Long teacherProfileId);
}
