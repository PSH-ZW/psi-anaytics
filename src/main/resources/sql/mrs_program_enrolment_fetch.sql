SELECT
    pp.patient_program_id as patient_program_id,
    pp.patient_id as patient_id,
    pp.program_id as program_id,
    p.name as program_name,
    pp.date_enrolled as date_enrolled,
    pp.date_completed as date_completed,
    pp.outcome_concept_id as outcome_concept_id,
    pp.uuid as uuid
FROM patient_program pp
    LEFT JOIN program p ON pp.program_id = p.program_id
where pp.patient_program_id = ?;