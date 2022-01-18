CREATE TABLE viac_form_template_8681(id serial PRIMARY KEY, vf_facility varchar, vf_site_type varchar, vf_district varchar, vf_setting varchar, vf_gravida varchar, vf_parity varchar, observations_vf_wgt varchar, observations_vf_t varchar, bp_vf_systolic varchar, bp_vf_diastolic varchar, observations_vf_p varchar, vf_type_of_visit varchar, vf_repeat_specify_period varchar, vf_treatment_specify_treatment varchar, vf_review_after_specify_details varchar, vf_hiv_status varchar, vf_is_the_client_on_treatment varchar, vf_which_line_of_treatment varchar, vf_combination_of_drugs varchar, vf_if_others_then_specify varchar, vf_date_of_initiation varchar, vf_latest_viral_load_result varchar, vf_reason_for_not_on_treatment varchar, vf_age_at_sexual_debut varchar, vf_no_of_lifetime_partners varchar, vf_presenting_complaints varchar, vf_presenting_complaints_specify_details varchar, vf_presenting_complaints_if_other_specify_details varchar, vf_ever_treated_for_stis varchar, vf_ever_treated_for_stis_specify_details varchar, vf_ever_treated_for_stis_if_other_specify_details varchar, vf_condom_use_with_regular_non_regular_partner varchar, vf_prior_family_planning_predominantly_used varchar, vf_have_you_ever_used_coc varchar, vf_coc_for_how_many_years varchar, vf_history_of_smoking varchar, vf_smoking_for_how_many_years varchar, vf_current_medication_for_chronic_conditions varchar, vf_chronic_conditions_specify_details varchar, vf_chronic_conditions_if_other_specify_details varchar, vf_prior_ca_cervix_screening_in_lifetime varchar, vf_when_were_you_last_screened varchar, vf_method_used_in_your_last_screen varchar, vf_screening_method_used_on_this_visit varchar, vf_specimen_collected_by varchar, vf_result varchar, vf_intervention varchar, vf_other_pathological_lesions_seen_vf_polyps varchar, vf_other_pathological_lesions_seen_vf_uterine_prolapse varchar, vf_other_pathological_lesions_seen_vf_nabothian_cysts varchar, vf_other_pathological_lesions_seen_vf_strawberry_lesions varchar, vf_other_pathological_lesions_seen_other varchar, vf_pathological_lesions_seen_if_other_specify_details varchar, picture_files_vf_image_one varchar, picture_files_vf_image_two varchar, picture_files_vf_image_three varchar, picture_files_vf_image_four varchar, picture_files_vf_image_five varchar, vf_treatment_given varchar, vf_treatment_given_specify_details varchar, vf_client_referred varchar, vf_referred_to varchar, vf_referred_for_public_sector varchar, vf_referred_for_if_other_specify_details varchar, vf_referred_for_pvt_sector varchar, vf_referred_for_if_other_specify_details_pvt_sector varchar, vf_referred_for_other_nsc varchar, vf_referred_for_if_other_specify_details_other_nsc varchar, vf_complications_adverse_events_during_procedure varchar, vf_adverse_events_during_procedure_specify_details varchar, vf_caedp_if_other_specify_details varchar, vf_next_appointment varchar, vf_answer_repeat_screening varchar, vf_repeat_screening_if_other_specify_details varchar, vf_answer_review varchar, vf_review_if_other_specify_details varchar, encounter_id integer, visit_id integer, patient_id integer, username varchar, date_created timestamp, patient_identifier varchar, location_id integer, location_name varchar, instance_id integer);

CREATE TABLE viac_form_template_8681_viac_form_service_provided (id serial PRIMARY KEY, vf_facility varchar, vf_site_type varchar, vf_district varchar, vf_setting varchar, parent_id integer, encounter_id integer, visit_id integer, patient_id integer, instance_id integer, provider_id integer, username varchar, date_created timestamp, patient_identifier varchar,location_id integer, location_name varchar);

CREATE TABLE viac_form_template_8681_viac_form_management (id serial PRIMARY KEY, vf_treatment_given varchar, vf_treatment_given_specify_details varchar, vf_client_referred varchar, vf_referred_to varchar, vf_referred_for_public_sector varchar, vf_referred_for_if_other_specify_details varchar, vf_referred_for_pvt_sector varchar, vf_referred_for_if_other_specify_details_pvt_sector varchar, vf_referred_for_other_nsc varchar, vf_referred_for_if_other_specify_details_other_nsc varchar, parent_id integer, encounter_id integer, visit_id integer, patient_id integer, instance_id integer, provider_id integer, username varchar, date_created timestamp, patient_identifier varchar,location_id integer, location_name varchar);

grant all privileges on table viac_form_template_8681 to analytics;
grant all privileges on table viac_form_template_8681_viac_form_management to analytics;
grant all privileges on table viac_form_template_8681_viac_form_service_provided to analytics;

grant usage, select on all sequences in schema public to analytics; --run this in analytics db as user postgres

insert into mapping(program_name, lookup_table, mapping_json, config) values('Patient', '{"instance":"patient"}',
'{"instance" : {"outreach_site_name":"gJcVOlb92FS","uic":"zRA08XEYiSF","gender":"AteZDFhkI0E","bithdate":"wSp6Q7QDMsk","residence":"Y1ZD9VjNofv","marital_status":"LopyyOT0FHa","occupation":"zW8FKylorqG","nationality":"KFvLq4usev1","education":"yIKK6uNLpBj","ethnicity":"U6K1PHFWDJa","district_of_birth":"HQOedKyjsdk","next_of_kin_address":"ZckwXrTWdx2","next_of_kin_telephone":"B3hSRRlA8ts","population":"oRtRjOifVZ5","contact_tracing":"CiTvAzwq3eu","index_client_code":"YiNIPRZdGR2","date_index_client_tested":"bcE9MqGH7y4","moonlight_testing":"HvNUMycHwJl","dreams_activity":"O6zCqRM450G","nearest_clinic_or_hospital":"iJixXXL0E4T","tested_for_hiv":"LWaFZ27oqD9","hiv_test_result":"XfQM8urodJJ","tested_in_the_last_twelve_months":"if8LjVC75bP","couple_testing":"zCOHwazkP0m","registration_number_of_partner" :"gAauAVU6bEJ"}}'
,'{"searchable":["uic"],"comparable":[],"openLatestCompletedEnrollment":"yes"}');

insert into mapping(program_name, lookup_table, mapping_json, config) values('Patient', '{"instance":"patient"}',
'{"formTableMappings" : {"patient" : {"outreach_site_name":"gJcVOlb92FS","uic":"zRA08XEYiSF","gender":"AteZDFhkI0E","bithdate":"wSp6Q7QDMsk","residence":"Y1ZD9VjNofv","marital_status":"LopyyOT0FHa","occupation":"zW8FKylorqG","nationality":"KFvLq4usev1","education":"yIKK6uNLpBj","ethnicity":"U6K1PHFWDJa","district_of_birth":"HQOedKyjsdk","next_of_kin_address":"ZckwXrTWdx2","next_of_kin_telephone":"B3hSRRlA8ts","population":"oRtRjOifVZ5","contact_tracing":"CiTvAzwq3eu","index_client_code":"YiNIPRZdGR2","date_index_client_tested":"bcE9MqGH7y4","moonlight_testing":"HvNUMycHwJl","dreams_activity":"O6zCqRM450G","nearest_clinic_or_hospital":"iJixXXL0E4T","tested_for_hiv":"LWaFZ27oqD9","hiv_test_result":"XfQM8urodJJ","tested_in_the_last_twelve_months":"if8LjVC75bP","couple_testing":"zCOHwazkP0m","registration_number_of_partner" :"gAauAVU6bEJ"}}}'
,'{"searchable":["uic"],"comparable":[],"openLatestCompletedEnrollment":"yes"}');

insert into mapping(program_name, mapping_json, config) values('Viac',
'{"formTableMappings" : {"viac_form_template_8681" : {"vf_setting":"Hc4iKERZLDk","vf_gravida":"UgU0MyC73ST","vf_parity":"JItRQLxfQ4A","vf_type_of_visit":"TgBqFU96ZHX","vf_repeat_specify_period":"RYkDTB2J6QO","vf_treatment_specify_treatment":"onIfDntdyv0","vf_hiv_status":"hh9Z0ymuKRZ",
 "vf_age_at_sexual_debut":"uBemM37lrOO","vf_no_of_lifetime_partners":"hysSfrFntQ7","vf_presenting_complaints":"nC39lIMfk98", "vf_ever_treated_for_stis":"GycGAisxIRC","vf_condom_use_with_regular_non_regular_partner":"SdqY1kZRNui","vf_prior_family_planning_predominantly_used":"QGbsKfo1CA9", "vf_have_you_ever_used_coc":"SUid7AxCFNJ","vf_history_of_smoking":"awZ6r6EZcCs","vf_current_medication_for_chronic_conditions":"LPlUY436Swn", "vf_prior_ca_cervix_screening_in_lifetime":"tRLHoPkaCdv","vf_when_were_you_last_screened":"qD7cp0yKJeZ", "vf_method_used_in_your_last_screen":"ZnnJrowYqmi","vf_screening_method_used_on_this_visit":"Um6zN7O1vzy", "vf_specimen_collected_by":"AdIDU0YDVOj","vf_result":"Oxhq5sfsc7G","vf_intervention":"ioNfmq39dS3"}}}', '{"searchable" :["UIC"],"comparable":[],"openLatestCompletedEnrollment":"yes"}');

