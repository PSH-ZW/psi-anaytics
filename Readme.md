##PSI-Analytics
###Project to sync data from OpenMrs to DHIS2

####Setting up analytics DB

1. Create a postgres container.
2. Create analytics db. 
   ~~~
    create database analytics;
    create user analytics with password 'password';
    grant all privileges on database analytics to analytics;
   ~~~
3. Run the run_liquibase.sh script in resources > liquibase > scripts

####Setting up Openmrs DB
1. Create a mysql container.
2. import the openmrs DB. 
3. Copy and run the functions in sql_functions.sql.

####Running the application
Run PsiAnalyticsApplication.java

---

###Overview
This project is used to flatten the hierarchical tables in openMRS(mysql DB) to an analytics DB(postgres).
The data from the analytics DB will be used for syncing with DHIS2.

####DB Access
We will be using jdbc templates for querying the DB. The datasources and jdbcTemplates are defined in DataSourceConfig.

####Batch jobs
  wip
####Flattening Process
 wip
 ####Handling MultiSelect inputs
 We will be creating a binary column for each of the values of the field. For the selected options we will set 't' to 
indicate they have been selected.
 
###Data Flow
#Patient
1. In bahmni we create a patient(event in openmrs) -> synced to analytics as patient and this raises an event 
2. Bahmni mart-dhis sync reads above event and pushes to dhis2 (we delete the event)
3. Dhis provides a unique id (tracked entity created)-> save in patient or mapping table

#Enrollment 
1. Patient getting enrolled to a program-> raises a program event and sync to analytics and raises an event.
2. Check whether patient is already enrolled in DHIS-2 server, by looking at program enrollment table.
3. If not enrolled -> enroll the patient and delete the event and update the uid to program enrollment table 
4. If enrolled -> delete the event

#Form-data
1. Whenever a form is filled it raises an encounter event in bahmni and this gets synced to analytics DB and raises an 
event 
2. Mart-Dhis2 sync either creates an event or updates an event based on the meta-data for encounter.

#Yet to discuss
1. Termination of an enrollment 