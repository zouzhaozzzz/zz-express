#!/bin/bash

APP_NAME=zz-gateway-server-9000.jar

nohup java -Xms128m -Xmx256m -jar ./lib/$APP_NAME -DprocessName=$APP_NAME  -Dfile.encoding=utf-8 -Duser.timezone=GMT+08 >log.log 2>&1 &
