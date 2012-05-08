#!/bin/sh

# only required on overlord machine (Ubuntu)
############################################


if [ "`id -u`" != "0" ]; then
  echo "This script must be run as root"
  exit 1
fi

echo "Installing the ansible overload dependencies"

apt-get install python-paramiko
apt-get install python-yaml
apt-get install python-jinja2

echo "Done installing ansible dependencies"

echo "Set DEPLOYMENT_HOME=${PWD}\n"
