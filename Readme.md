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
2. import the openmrs DB downloaded from [here](http://206.189.138.201/psi_openmrs.sql.gz). 
3. Copy and run the functions in sql_functions.sql.

####Running the application
Run PsiAnalyticsApplication.java

---

###Overview
This project is used for flattening the hierarchical tables in openMRS(mysql DB) to an analytics DB(postgres).
 We will be flattening data from `patient` (and related tables like person, person_address, person_attributes etc.),
`obs` and `program_enrollment` tables in openmrs database to analytics DB. The data from the analytics DB will be used for syncing to DHIS2.


####Program Flow
Whenever we perform operations in Bahmni related to adding a new patient, program enrollment or creating an encounter,
an event record will be created in the `event_records` table in OpenMRS DB. The `category` column contains the category of the event, 
eg patient, enrollment etc, and the `object` column contains the uuid reference of the column in the respected table. 

When we start the application, the JobScheduler gets triggered. It picks up cron jobs for Patient, Enrollment and Encounter from the analytics_cron_job table in analytics db and starts them.
It picks up a `FlatteningTask` bean with the name specified in the cronJob settings. We have three implementations for this FlatteningTask interface, `PatientTask`, `EncounterEventBasedJob` and `ProgramEnrollmentTask` for the flattening of patient, encounter and enrollment respectively.
The readers read the EventRecords, then get the corresponding object we need to flatten using the uuid from the `object` column, and send them to
the processors for processing (Our processor classes doesn't do much currently, they just pass the data to the writer). The writer classes do most 
of the processing. Writers generate the insert queries with the data values received from processors and insert them into the respective tables in analytics DB.
####DB Access
We will be using jdbc templates for querying the DB. The datasources and jdbcTemplates are defined in DataSourceConfig.
There are two datasources, one for the openmrs (mySql) DB which we will be reading from and one for analytics(postgres) DB,
which we will be writing to.

####Batch jobs
We will have batch jobs for processing `Patients`, `Encounters` and `Program Enrolments`. The reader, writer, processor and related classes for these are present in jobs package.

 ####Handling MultiSelect inputs
 We will be creating a binary column for each of the values of the field. For the selected options we will set 'true' to 
indicate they have been selected.
 
[//]: # (###Data Flow)

[//]: # (#Patient)

[//]: # (1. In bahmni we create a patient&#40;event in openmrs&#41; -> synced to analytics as patient and this raises an event )

[//]: # (2. Bahmni mart-dhis sync reads above event and pushes to dhis2 &#40;we delete the event&#41;)

[//]: # (3. Dhis provides a unique id &#40;tracked entity created&#41;-> save in patient or mapping table)

[//]: # ()
[//]: # (#Enrollment )

[//]: # (1. Patient getting enrolled to a program-> raises a program event and sync to analytics and raises an event.)

[//]: # (2. Check whether patient is already enrolled in DHIS-2 server, by looking at program enrollment table.)

[//]: # (3. If not enrolled -> enroll the patient and delete the event and update the uid to program enrollment table )

[//]: # (4. If enrolled -> delete the event)

[//]: # ()
[//]: # (#Form-data)

[//]: # (1. Whenever a form is filled it raises an encounter event in bahmni and this gets synced to analytics DB and raises an )

[//]: # (event )

[//]: # (2. Mart-Dhis2 sync either creates an event or updates an event based on the meta-data for encounter.)

###Forms used in each program
| Health Area | Forms in Bahmni                                                                                                       |
|-------------|-----------------------------------------------------------------------------------------------------------------------|
| ART         | Art initial Visit compulsory Question 1 of 2<br> Art initial Visit compulsory Question 2 of 2<br> Assessment and Plan |
| PrEP        | PrEP Screening Tool<br> PrEP Initial<br> PrEP Continuation                                                            |
| FP          | FP Counselling Only<br> Family Planning Initial<br> FP Continuation                                                   |
| HTS         | Provider HIV test counselling<br> HIV Self testing<br>                                                                |
| TB          | TB Screening and History                                                                                              |
| NCD         | NCD Template                                                                                                          |
| STI         | STI Symptoms                                                                                                          |
| VIAC        | VIAC Form Template                                                                                                    |
| Referrals   | Referrals template                                                                                                    |
| IPV         | IPV form                                                                                                              |
