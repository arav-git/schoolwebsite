# ADR 0001: Promotion Workflow

## Status
Accepted

## Context
Students should be promoted yearly based on final exam status. Failed students must not auto-promote.

## Decision
- Store exam status per student per academic year in `student_annual_results`.
- Only `PASS` is promotion-eligible.
- Record every promotion in `student_class_promotions`.
- Promotion updates student class and fee fields from `class_fee_structures`.
- Keep previous `fee_remaining` and add new class monthly due at promotion.
- Support both manual bulk promotion (admin UI) and scheduled auto promotion.

## Consequences
- Promotion behavior is auditable and deterministic.
- Data model supports yearly outcomes and retry-safe promotions.
- Business policy can change later by adjusting status gate logic.
