#!/bin/sh -x
nohup $JAVA_HOME/bin/java -jar $SERVER_OPTS /opt/psi-analytics/lib/psi-analytics.jar >> /var/log/psi-analytics/psi-analytics.log 2>&1 &
echo $! > /var/run/psi-analytics/psi-analytics.pid
