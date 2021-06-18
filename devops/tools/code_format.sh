#!/bin/sh

APP_PATH=$(cd `dirname $0`/../../; pwd)
APP_NAME=`echo $APP_PATH | awk -F "/" '{print $NF}'`
cd $APP_PATH

cd wayne-cloud-gateway
mvn clean compile

cd ../wayne-cloud-system
mvn clean compile

cd ../wayne-cloud-generate
mvn clean compile

cd ../wayne-cloud-web
mvn clean compile


