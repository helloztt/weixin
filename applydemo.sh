#!/usr/bin/env bash

mvn clean package
sftp alisz248.server.huobanplus.com <<EOF
put demo/target/demo.war /home/debuger/tomcat/webapps/wxtest.war
EOF
