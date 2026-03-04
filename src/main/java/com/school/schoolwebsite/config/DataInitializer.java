package com.school.schoolwebsite.config;

import com.school.schoolwebsite.entity.AppUser;
import com.school.schoolwebsite.entity.ClassFeeStructure;
import com.school.schoolwebsite.entity.Role;
import com.school.schoolwebsite.entity.SchoolClass;
import com.school.schoolwebsite.repository.ClassFeeStructureRepository;
import com.school.schoolwebsite.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner seedAdminUser(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        @Value("${app.admin.email}") String adminEmail,
        @Value("${app.admin.password}") String adminPassword,
        @Value("${app.admin.name}") String adminName
    ) {
        return args -> {
            if (userRepository.existsByRole(Role.ADMIN)) {
                return;
            }
            if (adminPassword == null || adminPassword.isBlank()) {
                log.warn("Skipping admin seed because APP_ADMIN_PASSWORD is not set");
                return;
            }

            AppUser admin = new AppUser();
            admin.setFullName(adminName);
            admin.setEmail(adminEmail.trim().toLowerCase());
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            admin.setJoiningDate(LocalDate.now());
            userRepository.save(admin);
        };
    }

    @Bean
    public CommandLineRunner seedClassFeeStructure(ClassFeeStructureRepository classFeeStructureRepository) {
        return args -> {
            for (SchoolClass schoolClass : SchoolClass.values()) {
                if (classFeeStructureRepository.existsBySchoolClass(schoolClass)) {
                    continue;
                }

                int multiplier = schoolClass.ordinal() + 1;
                BigDecimal feePerMonth = BigDecimal.valueOf(1600L + (multiplier * 250L));
                BigDecimal busPerMonth = BigDecimal.valueOf(700L + (multiplier * 80L));
                BigDecimal busYearly = busPerMonth.multiply(BigDecimal.valueOf(10L));

                ClassFeeStructure feeStructure = new ClassFeeStructure();
                feeStructure.setSchoolClass(schoolClass);
                feeStructure.setFeePerMonth(feePerMonth);
                feeStructure.setBusFeePerMonth(busPerMonth);
                feeStructure.setBusFeeYearly(busYearly);
                classFeeStructureRepository.save(feeStructure);
            }
        };
    }
}
