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
  pi_uic.identifier as UIC,
  pn.given_name,
  pn.middle_name,
  pn.family_name,
  get_attribute_value('Telephone') as telephone,
  get_attribute_value('Other specific (occupation)') as occupation_specific,
  get_attribute_value('Mother''s name') as mothers_name,
  get_attribute_value('Next-of-kin Address') as next_of_kin_address,
  get_attribute_value('If couple testing yes,capture the registration no.') as registration_number_of_partner,
  get_attribute_value('Nearest Clinic/Hospital') as nearest_clinic_or_hospital,
  get_attribute_value('Outreach site name') as outreach_site_name,
  get_attribute_value('Index Client Code') as index_client_code,
  get_attribute_value('Next-of-kin Telephone Number') as next_of_kin_telephone,
  get_attribute_value('If relationship to next to kin other, then specify') as next_of_kin_relationship_specific,
  get_attribute_value('Next-of-kin Contact Name') as next_of_kin_contact_name,
  get_attribute_value('National ID Number') as nationality_id_number,
  get_concept_name(get_attribute_value('Nationality')) as nationality,
  get_concept_name(get_attribute_value('Couple testing ?couple_test')) as couple_testing,
  get_concept_name(get_attribute_value('Ever been tested in the last twelve months')) as tested_in_last_twelve_months,
  get_concept_name(get_attribute_value('If yes for ever been tested for HIV, Result?')) as hiv_test_result,
  get_concept_name(get_attribute_value('Have you ever been tested for HIV')) as tested_for_hiv,
  get_concept_name(get_attribute_value('Dreams Activity')) as dreams_activity,
  get_concept_name(get_attribute_value('Moonlight Testing')) as moonlight_testing,
  get_concept_name(get_attribute_value('Outreach Classification')) as outreach_classification,
  get_concept_name(get_attribute_value('Site Type')) as site_type,
  get_concept_name(get_attribute_value('Relationship to Index')) as index_relationship,
  get_concept_name(get_attribute_value('Contact Tracing')) as contact_tracing,
  get_concept_name(get_attribute_value('Referral source')) as referral_source,
  get_concept_name(get_attribute_value('Population')) as population,
  get_concept_name(get_attribute_value('Relationship to next of kin')) as next_of_kin_relationship,
  get_concept_name(get_attribute_value('If yes, are you the firstborn?')) as firstborn,
  get_concept_name(get_attribute_value('Are you a twin')) as twin,
  get_concept_name(get_attribute_value('District of Birth')) as district_of_birth,
  get_concept_name(get_attribute_value('Client Resident')) as client_resident,
  get_concept_name(get_attribute_value('Marital Status')) as marital_status,
  get_concept_name(get_attribute_value('Ethnicity')) as ethnicity,
  get_concept_name(get_attribute_value('occupation')) as occupation,
  get_concept_name(get_attribute_value('education')) as education
from person per
  inner join patient pat on pat.patient_id=per.person_id
  inner join patient_identifier pi_uic on pi_uic.patient_id=per.person_id and pi_uic.identifier_type = (select patient_identifier_type_id from patient_identifier_type where name = 'UIC')
  inner join person_name pn on pn.person_id=per.person_id and pn.preferred = true
  inner join patient_identifier pi on pi.patient_id=per.person_id and pi.preferred = true
  left join person_address pa on pa.person_id=per.person_id and pa.preferred = true
where per.person_id = ?;
