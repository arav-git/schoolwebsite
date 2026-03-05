# School Website Project Context

## Stack
- Java 17
- Spring Boot 3.x
- Spring Security + Thymeleaf
- Spring Data JPA (Hibernate)
- PostgreSQL

## Auth and roles
- Roles: `ADMIN`, `TEACHER`, `STUDENT`
- Login by email/password (BCrypt)
- Role redirects via `/portal`

## Core routes
- Public: `/`, `/about`, `/register`, `/login`
- Admin: `/admin/**`
- Teacher: `/teacher/**`
- Student: `/student/**`

## Main data model
- `users` (auth table): id, full_name, email, password, role, joining_date
- `student_profiles`: user_id, school_class, fee_per_month, fee_remaining, bus_fee_per_month, bus_fee_yearly
- `teacher_profiles`: user_id, salary_per_month, salary_paid
- `class_fee_structures`: school_class, fee_per_month, bus_fee_per_month, bus_fee_yearly
- `student_fee_payments`: student fee history
- `teacher_salary_payments`: teacher salary history
- `student_annual_results`: academic_year + exam_status (`PASS/FAIL/PENDING`)
- `student_class_promotions`: promotion history log

## Current business logic
- Student registration creates profile from class fee structure.
- Teacher registration creates profile with salary values editable by admin later.
- Admin can edit student/teacher details from UI.
- Teacher/Admin can manage student exam status by class and academic year.
- Only `PASS` students are eligible for promotion.
- Promotion updates class + fee fields and stores promotion history.
- Auto promotion scheduler exists and runs on configured date.

## Auto promotion config
- `APP_PROMOTION_AUTO_ENABLED` (default `true`)
- `APP_PROMOTION_AUTO_DAY_MONTH` (default `04-01`)
- `APP_PROMOTION_AUTO_CRON` (default `0 30 2 * * *`)

## Security boundaries
- Teacher class list: only `name/email/joiningDate/class`.
- Fee/salary data restricted to owner + admin views.
