#!/bin/bash

if [ `id -u` -ne 0 ]; then
    echo "This script can only be run as root"
    exit 1
fi

if [ "$1" == "" ]; then
  echo "call installer with the project name: remove.sh <project name>"
  exit 1
else
  PROJECT_NAME=$1
fi

if [ "$PROJECT_DIR" == "" ]; then
  PROJECT_DIR=`pwd`
fi

TARGET_DIR=/usr/local/share/$PROJECT_NAME

#deluser --system $PROJECT_NAME
#deluser --system --group $PROJECT_NAME
deluser $PROJECT_NAME
deluser --group $PROJECT_NAME
rm -rf $TARGET_DIR
rm -rf /var/run/$PROJECT_NAME
rm -rf /var/log/$PROJECT_NAME

