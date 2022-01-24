--OpenMRS DB
DROP FUNCTION IF EXISTS get_concept_name;
DELIMITER $$
CREATE FUNCTION get_concept_name(concept_id_str varchar(50))
RETURNS varchar(255)
DETERMINISTIC
BEGIN
   DECLARE con_name varchar(255);
   SELECT name into con_name from concept_name where concept_id =  cast(concept_id_str as UNSIGNED)
   and concept_name_type = 'FULLY_SPECIFIED' and locale = 'en';
   IF con_name = 'Yes' THEN
       SET con_name = 'true';
   ELSEIF con_name = 'No' THEN
       SET con_name = 'false';
   End IF;
   RETURN con_name;
END $$
DELIMITER;
-----------------------------------------

--Analytics DB
--reset synced events;
update events_to_sync set synced = false;
delete from processed_events;

--clear all data in analytics
delete from patient;
delete from encounter;
delete from viac_form_template_8681;
delete from events_to_sync;
delete from instance_tracker;
delete from orgunit_tracker;
delete from processed_events;
delete from program_enrolment;
