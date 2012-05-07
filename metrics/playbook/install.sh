#!/bin/bash

VERSION="0.0.0.1"
ansible-playbook -D -u ubuntu -e "version=$VERSION" playbook/install_pb.yml

