#!/bin/bash

#stop the server
service psi-analytics stop || true
rm -f /etc/httpd/conf.d/psi_analytics_ssl.conf
