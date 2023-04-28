#!/bin/bash

APP_NAME=zz-gateway-server-9000.jar

PID=$(ps -ef | grep $APP_NAME | grep -v grep | awk '{ print $2 }')
if [ -z "$PID" ]
then
    echo 应用$APP_NAME未启动或已经停止
else
    echo 正在结束应用$APP_NAME进程[$PID]...
    kill -15 $PID
    sleep 5s
    process=$(ps -ef | grep $APP_NAME | grep -v grep | awk '{ print $2 }')
    if [ -z "$process" ]
    then 
        echo 应用$APP_NAME进程[$PID]停止成功
    else
        echo 应用$APP_NAME进程[$PID]正常停止失败，现在强制杀死进程
        kill -9 $process
        if [ $? -eq 0 ]
        then
                echo 应用$APP_NAME进程[$process]强制停止成功
        else
                echo 应用$APP_NAME进程[$process]强制停止失败，请手动检查
        fi
    fi
fi
