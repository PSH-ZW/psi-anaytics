#!/bin/bash

if [ -f /etc/bahmni-installer/bahmni.conf ]; then
. /etc/bahmni-installer/bahmni.conf
fi

#create bahmni user and group if doesn't exist
USERID=bahmni
GROUPID=bahmni
/bin/id -g $GROUPID 2>/dev/null
[ $? -eq 1 ]
groupadd bahmni

/bin/id $USERID 2>/dev/null
[ $? -eq 1 ]
useradd -g bahmni bahmni

mkdir -p /bahmni_temp/logs
chown -R bahmni:bahmni /bahmni_temp
chmod -R 766 /bahmni_temp

#create links
ln -s /opt/psi-analytics/etc /etc/psi-analytics
ln -s /opt/psi-analytics/bin/psi-analytics /etc/init.d/psi-analytics
ln -s /opt/psi-analytics/run /var/run/psi-analytics
ln -s /opt/psi-analytics/psi-analytics /var/run/psi-analytics/psi-analytics
ln -s /opt/psi-analytics/log /var/log/psi-analytics

rm -f /etc/apache2/conf-enabled/PSI_ANALYTICS_ssl.conf
ln -s /opt/psi-analytics/etc/PSI_ANALYTICS_ssl.conf /etc/apache2/conf-enabled/PSI_ANALYTICS_ssl.conf

rm -f /opt/psi-analytics/psi-analytics/WEB-INF/classes/application.properties
cp /opt/psi-analytics/etc/application.properties /opt/psi-analytics/psi-analytics/WEB-INF/classes/

#chkconfig --add psi-analytics
cp /opt/psi-analytics/bin/psi-analytics.service /lib/systemd/system
chmod 777 /lib/systemd/system/psi-analytics.service

systemctl daemon-reload
systemctl enable psi-analytics.service

# permissions
chown -Rf bahmni:bahmni /opt/psi-analytics
chown -R bahmni:bahmni /var/log/psi-analytics
chown -R bahmni:bahmni /var/run/psi-analytics
chown -R bahmni:bahmni /etc/init.d/psi-analytics
chown -R bahmni:bahmni /etc/psi-analytics

mkdir -p /home/bahmni/psi/analytics/uploads
ln -s /home/bahmni/psi/analytics/uploads /opt/psi-analytics/uploads

chown -R bahmni:bahmni /home/bahmni/psi/analytics/uploads
chown -R bahmni:bahmni /opt/psi-analytics/uploads

#export JAVA_HOME=/opt/java/jdk-10.0.2/
