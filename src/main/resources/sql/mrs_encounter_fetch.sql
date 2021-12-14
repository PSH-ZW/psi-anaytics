SELECT
  en.encounter_id as encounter_id,
  en.encounter_type as encounter_type_id,
  en.encounter_datetime as encounter_date_created,
  en.visit_id as visit_id,
  en.patient_id as patient_id,
  en.location_id as location_id,
  l.name as location_name,
  en.uuid as uuid
FROM encounter en
  LEFT JOIN location l ON en.location_id = l.location_id
WHERE en.encounter_id = ?;
