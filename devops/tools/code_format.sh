#!/bin/sh

APP_PATH=$(cd `dirname $0`/../../; pwd)
APP_NAME=`echo $APP_PATH | awk -F "/" '{print $NF}'`
cd $APP_PATH

cd portal-gateway
mvn clean compile

cd ../portal-server
mvn clean compile

cd ../sso-server
mvn clean compile

#cd ../oauth2-client
#mvn clean compile


