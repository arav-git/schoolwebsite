# Handover

## Last updated
- 2026-03-05

## Completed
- DPS-style UI upgrade across public/auth/dashboard pages.
- DB split into auth (`users`) + student/teacher profile tables.
- Admin edit flows for student and teacher financial/profile fields.
- Student class fee recalculation from fee structure when class changes.
- Teacher class-wise student management page with filters.
- Exam status workflow (`PASS/FAIL/PENDING`) by class/year.
- Bulk promotion + auto promotion (PASS-only) + promotion history.
- Project memory files and agent instructions added.

## Pending / next improvements
- Add automated tests for promotion/status/edit workflows.
- Add audit trail table for sensitive financial edits.
- Add pagination/search for large student lists.
- Add admin UI for class fee structure management.

## Environment needed
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `APP_ADMIN_EMAIL`, `APP_ADMIN_PASSWORD`, `APP_ADMIN_NAME`
- Optional promotion env vars in `application.properties`
