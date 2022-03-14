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
-----
--TODO:instead of droppinng and creating a new table everytime, delete from the table.
DROP PROCEDURE IF EXISTS get_attributes;
DELIMITER //
CREATE PROCEDURE get_attributes(personId int)
BEGIN
    DELETE FROM person_attribute_temp;
    INSERT INTO person_attribute_temp(person_id, value, attribute_type) select pa.person_id, pa.value as value, pat.name as attribute_type from person_attribute pa
    left join person_attribute_type pat on pa.person_attribute_type_id = pat.person_attribute_type_id
    where pa.voided = 0 and pa.person_id = personId;
END
//

-----------
DROP FUNCTION IF EXISTS get_attribute_value;
DELIMITER $$
CREATE FUNCTION get_attribute_value(attribute_type_name varchar(50), person_id int)
    RETURNS varchar(255)
    DETERMINISTIC
BEGIN
   DECLARE attr_value varchar(255);

SELECT coalesce(value, '') into attr_value from person_attribute pa
    left join person_attribute_type pat on pa.person_attribute_type_id = pat.person_attribute_type_id
    where pa.voided = 0 and pa.person_id = person_id and pat.name = attribute_type_name;

RETURN attr_value;
END $$
DELIMITER;
