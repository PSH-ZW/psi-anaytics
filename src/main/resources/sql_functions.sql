DROP FUNCTION IF EXISTS get_concept_name;
DELIMITER $$
CREATE FUNCTION get_concept_name(concept_id_str varchar(50))
RETURNS varchar(255)
DETERMINISTIC
BEGIN
   DECLARE con_name varchar(255);
   SELECT name into con_name from concept_name where concept_id =  cast(concept_id_str as UNSIGNED) and concept_name_type = 'FULLY_SPECIFIED' and locale = 'en';
   RETURN con_name;
END $$
DELIMITER;
-----------------------------------------