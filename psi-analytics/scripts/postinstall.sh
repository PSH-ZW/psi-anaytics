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

rm -f /etc/httpd/conf.d/nuacare_analytics_ssl.conf
ln -s /opt/psi-analytics/etc/nuacare_analytics_ssl.conf /etc/httpd/conf.d/nuacare_analytics_ssl.conf

rm -f /opt/psi-analytics/psi-analytics/WEB-INF/classes/application.properties
cp /opt/psi-analytics/etc/application.properties /opt/psi-analytics/psi-analytics/WEB-INF/classes/

chkconfig --add psi-analytics


# permissions
chown -Rf bahmni:bahmni /opt/psi-analytics
chown -R bahmni:bahmni /var/log/psi-analytics
chown -R bahmni:bahmni /var/run/psi-analytics
chown -R bahmni:bahmni /etc/init.d/psi-analytics
chown -R bahmni:bahmni /etc/psi-analytics

mkdir -p /home/bahmni/nuacare/analytics/uploads
ln -s /home/bahmni/nuacare/analytics/uploads /opt/psi-analytics/uploads

chown -R bahmni:bahmni /home/bahmni/nuacare/analytics/uploads
chown -R bahmni:bahmni /opt/psi-analytics/uploads

