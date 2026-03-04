package com.school.schoolwebsite.repository;

import com.school.schoolwebsite.entity.StudentFeePayment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentFeePaymentRepository extends JpaRepository<StudentFeePayment, Long> {

    List<StudentFeePayment> findTop10ByStudentProfileIdOrderByPaidOnDesc(Long studentProfileId);
}
