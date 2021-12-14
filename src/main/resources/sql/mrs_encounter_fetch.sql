SELECT
  en.encounter_id as encounter_id,
  en.encounter_type as encounter_type_id,
  et.name as encounter_type_name,
  en.encounter_datetime as encounter_date_created,
  en.visit_id as visit_id,
  en.patient_id as patient_id,
  pit.identifier as patient_identifier,
  en.location_id as location_id,
  l.name as location_name,
  pro.provider_id as provider_id,
  pro.name as provider,
  en.uuid as uuid
FROM encounter en
  LEFT JOIN encounter_type et ON en.encounter_type = et.encounter_type_id
  LEFT JOIN location l ON en.location_id = l.location_id
  LEFT JOIN patient_identifier pit ON en.patient_id = pit.patient_id
  LEFT JOIN encounter_provider enp ON enp.encounter_id = en.encounter_id
  LEFT JOIN provider pro ON pro.provider_id = enp.provider_id
WHERE en.encounter_id = ?;
