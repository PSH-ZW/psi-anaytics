#!/bin/sh
set -e -x
BASEDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CLASSPATH="${BASEDIR}/../lib/mysql-connector-java-5.1.28.jar:${BASEDIR}/../changesets"

CHANGE_LOG_TABLE="-Dliquibase.databaseChangeLogTableName=liquibasechangelog -Dliquibase.databaseChangeLogLockTableName=liquibasechangeloglock"
LIQUIBASE_JAR="${BASEDIR}/../lib/liquibase-core-2.0.5.jar"
echo "liquibase jar"

DRIVER="com.mysql.jdbc.Driver"
CREDS="--url=jdbc:mysql://localhost:3306/openmrs --username=root --password=password"
CHANGE_LOG_FILE="${BASEDIR}/../changesets/openmrs_liquibase.xml"

java $CHANGE_LOG_TABLE  -jar $LIQUIBASE_JAR --driver=$DRIVER --classpath=$CLASSPATH --logLevel=debug --changeLogFile=$CHANGE_LOG_FILE $CREDS update
