#!/bin/sh
set -e -x
BASEDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CLASSPATH="${BASEDIR}/../lib/postgresql-9.4-1206-jdbc42.jar:${BASEDIR}/../changesets"

CHANGE_LOG_TABLE="-Dliquibase.databaseChangeLogTableName=liquibasechangelog -Dliquibase.databaseChangeLogLockTableName=liquibasechangeloglock"
LIQUIBASE_JAR="${BASEDIR}/../lib/liquibase-core-3.5.3.jar"
echo "liquibase jar"
DRIVER="org.postgresql.Driver"
CREDS="--url=jdbc:postgresql://localhost:5432/analytics --username=postgres --password=password"
CHANGE_LOG_FILE="${BASEDIR}/../changesets/liquibase.xml"

java $CHANGE_LOG_TABLE  -jar $LIQUIBASE_JAR --driver=$DRIVER --classpath=$CLASSPATH --logLevel=debug --changeLogFile=$CHANGE_LOG_FILE $CREDS update
