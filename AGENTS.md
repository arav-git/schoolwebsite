# Project Agent Guide

## First Read (required)
1. `docs/PROJECT_CONTEXT.md`
2. `docs/HANDOVER.md`
3. `docs/adr/0001-promotion-workflow.md`

## Working rules
- Keep existing Spring routes and role access behavior unless explicitly requested.
- Never hardcode secrets in source. Use env vars from `application.properties` placeholders.
- Use `SchoolClass` + `class_fee_structures` as source of truth for student fee defaults.
- Promotion logic must enforce final exam status checks (`PASS` only).
- Teacher views must never expose fee/salary details.
- Student/Teacher dashboards show only own data; admin can view all.

## Run/verify
- Compile: `./mvnw -DskipTests compile`
- Run: `./mvnw spring-boot:run`

## Notes for future Codex sessions
- Update `docs/HANDOVER.md` at end of major changes.
- Record architecture decisions under `docs/adr/`.
