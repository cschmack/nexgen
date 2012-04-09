#!/bin/bash

SCRIPT_DIR=`dirname $0`

$SCRIPT_DIR/remove.sh $1

$SCRIPT_DIR/install.sh $1
