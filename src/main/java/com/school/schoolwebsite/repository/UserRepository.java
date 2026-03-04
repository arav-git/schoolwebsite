package com.school.schoolwebsite.repository;

import com.school.schoolwebsite.entity.AppUser;
import com.school.schoolwebsite.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByRole(Role role);
}
