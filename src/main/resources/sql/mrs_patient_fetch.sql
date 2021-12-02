select
  per.person_id as patient_id,
  per.gender,
  per.birthdate,
  per.dead,
  per.death_date,
  per.date_created,
  per.uuid,
  per.voided as inactive,
  per.void_reason as deactivated_reason,
  per.date_voided as date_deactivated,
  pa.address1,
  pa.address2,
  pa.address3,
  pa.address4,
  pa.address5,
  pa.address6,
  pa.city_village,
  pa.state_province,
  pa.postal_code,
  pa.country,
  pa.county_district,
  pi.identifier as patient_identifier,
  pn.given_name,
  pn.middle_name,
  pn.family_name,
from person per
  inner join patient pat on pat.patient_id=per.person_id
  left join person_address pa on pa.person_id=per.person_id and pa.preferred = true
  left join patient_identifier pi on pi.patient_id=per.person_id and pi.preferred = true
  left join person_name pn on pn.person_id=per.person_id and pn.preferred = true
where per.person_id = ?;
