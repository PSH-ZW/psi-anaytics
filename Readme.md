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