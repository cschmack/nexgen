#!/bin/bash
## temporary installation to just lay down the files in the right place

if [ `id -u` -ne 0 ]; then
    echo "This script can only be run as root"
    exit 1
fi


if [ "$PROJECT_DIR" == "" ]; then
  PROJECT_DIR=`pwd`
fi 

if [ "$1" == "" ]; then
  echo "call installer with the project name: install.sh <project name>"
  exit 1
else
  PROJECT_NAME=$1
fi

## TODO: create a user to run under

#adduser --quiet --system --no-create-home --disabled-login --disabled-password --group $PROJECT_NAME
#adduser --quiet --system --no-create-home --disabled-password --group $PROJECT_NAME
#adduser --quiet --system --no-create-home --group $PROJECT_NAME
adduser --quiet --no-create-home --disabled-login --gecos $PROJECT_NAME $PROJECT_NAME

TARGET_DIR=/usr/local/share/$PROJECT_NAME
mkdir -p $TARGET_DIR 
cp -R $PROJECT_DIR/dist/* $TARGET_DIR/
cp -R $PROJECT_DIR/dist/*.jar $TARGET_DIR/lib

chmod +x $TARGET_DIR/bin/*
chown -R $PROJECT_NAME:$PROJECT_NAME $TARGET_DIR

