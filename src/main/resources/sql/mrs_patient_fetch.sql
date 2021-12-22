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
  tele_attr.value as telephone,
  spec_occu.value as occupation_specific,
  mother_name.value as mothers_name,
  kin_add.value as next_of_kin_address,
  reg_no.value as registration_number_of_partner,
  clinic_hos.value as nearest_clinic_or_hospital,
  site_name.value as outreach_site_name,
  index_code.value as index_client_code,
  kin_tel.value as next_of_kin_telephone,
  oth_rel.value as next_of_kin_relationship_specific,
  kin_name.value as next_of_kin_contact_name,
  nat_id.value as nationality_id_number,
  get_concept_name(nationality_attr.value) as nationality,
  get_concept_name(couple_test_attr.value) as couple_testing,
  get_concept_name(twelve_month_attr.value) as tested_in_last_twelve_months,
  get_concept_name(hiv_res_attr.value) as hiv_test_result,
  get_concept_name(hiv_test_attr.value) as tested_for_hiv,
  get_concept_name(dreams_attr.value) as dreams_activity,
  get_concept_name(moon_attr.value) as moonlight_testing,
  get_concept_name(outreach_class_attr.value) as outreach_classification,
  get_concept_name(site_type_attr.value) as site_type,
  get_concept_name(ind_rel_attr.value) as index_relationship,
  get_concept_name(cont_trace_attr.value) as contact_tracing,
  get_concept_name(reff_source_attr.value) as referral_source,
  get_concept_name(population_attr.value) as population,
  get_concept_name(kin_rel_attr.value) as next_of_kin_relationship,
  get_concept_name(firstborn_attr.value) as firstborn,
  get_concept_name(is_twin_attr.value) as twin,
  get_concept_name(distr_birth_attr.value) as district_of_birth,
  get_concept_name(client_resident_attr.value) as client_resident,
  get_concept_name(mar_stat_attr.value) as marital_status,
  get_concept_name(ethnicity_attr.value) as ethnicity,
  get_concept_name(occupation_attr.value) as occupation,
  get_concept_name(education_attr.value) as education
from person per
  inner join patient pat on pat.patient_id=per.person_id
  left join person_address pa on pa.person_id=per.person_id and pa.preferred = true
  left join patient_identifier pi on pi.patient_id=per.person_id and pi.preferred = true
  left join patient_identifier pi_uic on pi_uic.patient_id=per.person_id and pi_uic.identifier_type = (select patient_identifier_type_id from patient_identifier_type where name = 'UIC')
  left join person_name pn on pn.person_id=per.person_id and pn.preferred = true
  left join person_attribute tele_attr on tele_attr.person_id = per.person_id and tele_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Telephone')
  left join person_attribute nationality_attr on nationality_attr.person_id = per.person_id and nationality_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Nationality')
  left join person_attribute education_attr on education_attr.person_id = per.person_id and education_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'education')
  left join person_attribute occupation_attr on occupation_attr.person_id = per.person_id and occupation_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'occupation')
  left join person_attribute ethnicity_attr on ethnicity_attr.person_id = per.person_id and ethnicity_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Ethnicity')
  left join person_attribute mar_stat_attr on mar_stat_attr.person_id = per.person_id and mar_stat_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Marital Status')
  left join person_attribute client_resident_attr on client_resident_attr.person_id = per.person_id and client_resident_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Client Resident')
  left join person_attribute distr_birth_attr on distr_birth_attr.person_id = per.person_id and distr_birth_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'District of Birth')
  left join person_attribute is_twin_attr on is_twin_attr.person_id = per.person_id and is_twin_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Are you a twin')
  left join person_attribute firstborn_attr on firstborn_attr.person_id = per.person_id and firstborn_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'If yes, are you the firstborn?')
  left join person_attribute kin_rel_attr on kin_rel_attr.person_id = per.person_id and kin_rel_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Relationship to next of kin')
  left join person_attribute population_attr on population_attr.person_id = per.person_id and population_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Population')
  left join person_attribute reff_source_attr on reff_source_attr.person_id = per.person_id and reff_source_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Referral source')
  left join person_attribute cont_trace_attr on cont_trace_attr.person_id = per.person_id and cont_trace_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Contact Tracing')
  left join person_attribute ind_rel_attr on ind_rel_attr.person_id = per.person_id and ind_rel_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Relationship to Index')
  left join person_attribute site_type_attr on site_type_attr.person_id = per.person_id and site_type_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Site Type')
  left join person_attribute outreach_class_attr on outreach_class_attr.person_id = per.person_id and outreach_class_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Outreach Classification')
  left join person_attribute moon_attr on moon_attr.person_id = per.person_id and moon_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Moonlight Testing')
  left join person_attribute dreams_attr on dreams_attr.person_id = per.person_id and dreams_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Dreams Activity')
  left join person_attribute hiv_test_attr on hiv_test_attr.person_id = per.person_id and hiv_test_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Have you ever been tested for HIV')
  left join person_attribute hiv_res_attr on hiv_res_attr.person_id = per.person_id and hiv_res_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'If yes for ever been tested for HIV, Result?')
  left join person_attribute twelve_month_attr on twelve_month_attr.person_id = per.person_id and twelve_month_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Ever been tested in the last twelve months')
  left join person_attribute couple_test_attr on couple_test_attr.person_id = per.person_id and couple_test_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Couple testing ?couple_test')
  left join person_attribute spec_occu on spec_occu.person_id = per.person_id and spec_occu.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Other specific (occupation)')
  left join person_attribute nat_id on nat_id.person_id = per.person_id and nat_id.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'National ID Number')
  left join person_attribute mother_name on mother_name.person_id = per.person_id and mother_name.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Mother\'s name')
  left join person_attribute kin_name on kin_name.person_id = per.person_id and kin_name.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Next-of-kin Contact Name')
  left join person_attribute oth_rel on oth_rel.person_id = per.person_id and oth_rel.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'If relationship to next to kin other, then specify')
  left join person_attribute kin_add on kin_add.person_id = per.person_id and kin_add.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Next-of-kin Address')
  left join person_attribute kin_tel on kin_tel.person_id = per.person_id and kin_tel.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Next-of-kin Telephone Number')
  left join person_attribute index_code on index_code.person_id = per.person_id and index_code.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Index Client Code')
  left join person_attribute site_name on site_name.person_id = per.person_id and site_name.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Outreach site name')
  left join person_attribute clinic_hos on clinic_hos.person_id = per.person_id and clinic_hos.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'Nearest Clinic/Hospital')
  left join person_attribute reg_no on reg_no.person_id = per.person_id and reg_no.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'If couple testing yes,capture the registration no.')
where per.person_id = ?;
