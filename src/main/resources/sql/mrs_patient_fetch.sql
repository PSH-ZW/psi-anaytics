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
  mob_attr.value   as mobile,
  email_attr.value as email,
  gws_attr.value   as gws_id,
  lat_attr.value   as latitude,
  long_attr.value  as longitude,
  penb_attr.value  as pension_band,
  ins_attr.value   as insurance_number,
  army_attr.value  as army_id,
  rel_attr.value   as relation,
  bname_attr.value as bank_name,
  bnum_attr.value  as bank_account_number,
  acc_holder_attr.value as account_holder_name,
  line_attr.value as landline,
  pen_attr.value as pension_type,
  rel_par_attr.value as relation_parent,
  id_code_attr.value as id_short_code,
  pat.merged as merged,
  awc_loc.name   as awc_name
from person per
  inner join patient pat on pat.patient_id=per.person_id
  left join person_address pa on pa.person_id=per.person_id and pa.preferred = true
  left join patient_identifier pi on pi.patient_id=per.person_id and pi.preferred = true
  left join person_name pn on pn.person_id=per.person_id and pn.preferred = true
  left join person_attribute mob_attr on mob_attr.person_id=per.person_id and mob_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'mobile')
  left join person_attribute email_attr on email_attr.person_id=per.person_id and email_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'email')
  left join person_attribute ins_attr on ins_attr.person_id=per.person_id and ins_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'insuranceNumber')
  left join person_attribute gws_attr on gws_attr.person_id=per.person_id and gws_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'gwsId')
  left join person_attribute long_attr on long_attr.person_id=per.person_id and long_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'longitude')
  left join person_attribute lat_attr on lat_attr.person_id=per.person_id and lat_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'latitude')
  left join person_attribute penb_attr on penb_attr.person_id=per.person_id and penb_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'pensionBand')
  left join person_attribute army_attr on army_attr.person_id = per.person_id and army_attr.person_attribute_type_id = (select person_attribute_type_id from person_attribute_type where name = 'armyId')
  left join person_attribute rel_attr on rel_attr.person_id = per.person_id and rel_attr.person_attribute_type_id = (select person_attribute_type_id from person_attribute_type where name = 'relation')
  left join person_attribute bname_attr on bname_attr.person_id = per.person_id and bname_attr.person_attribute_type_id = (select person_attribute_type_id from person_attribute_type where name = 'bankName')
  left join person_attribute bnum_attr on bnum_attr.person_id = per.person_id and bnum_attr.person_attribute_type_id = (select person_attribute_type_id from person_attribute_type where name = 'bankACNumber')
  left join person_attribute acc_holder_attr on acc_holder_attr.person_id = per.person_id and acc_holder_attr.person_attribute_type_id = (select person_attribute_type_id from person_attribute_type where name = 'accountHolderName')
  left join person_attribute bbranch_attr on bbranch_attr.person_id = per.person_id and bbranch_attr.person_attribute_type_id = (select person_attribute_type_id from person_attribute_type where name = 'bankBranchCode')
  left join person_attribute line_attr on line_attr.person_id = per.person_id and line_attr.person_attribute_type_id = (select person_attribute_type_id from person_attribute_type where name = 'landline')
  left join person_attribute rel_par_attr on rel_par_attr.person_id = per.person_id and rel_par_attr.person_attribute_type_id = (select person_attribute_type_id from person_attribute_type where name = 'relationParent')
  left join person_attribute pen_attr on pen_attr.person_id = per.person_id and pen_attr.person_attribute_type_id = (select person_attribute_type_id from person_attribute_type where name = 'pensionType')
  left join person_attribute id_code_attr on id_code_attr.person_id = per.person_id and id_code_attr.person_attribute_type_id = (select person_attribute_type_id from person_attribute_type where name = 'idShortCode')
  left join person_attribute awc_attr on awc_attr.person_id=per.person_id and awc_attr.person_attribute_type_id=(select person_attribute_type_id from person_attribute_type where name = 'AWC Name')
  left join location awc_loc on awc_attr.value = awc_loc.uuid
where per.person_id = ?;
