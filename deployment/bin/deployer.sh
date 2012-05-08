#!/bin/bash
###########


PLAYBOOK_DIR="playbook"
SCRIPT=`basename $0`

# print usage of the script
function usage() {
  echo -e "\nNexgen deployer script\n"
  echo -e "$SCRIPT <version> <environment> <command>\n" >&2
  echo -e "\nThis script should be run from within a project path"
  echo -e "It assumes there is a $PLAYBOOK_DIR directory containing: \n"
  echo -e "\t./$PLAYBOOK_DIR/"
  echo -e "\t\t- <environment>.ansible.hosts"
  echo -e "\t\t- <command>.yml "
  echo 
  exit 1
}

VERSION="0.0.0.1"
ENV_HOSTS=""
PLAYBOOK=""

if [ $# -lt 3 ]; then
  usage
else
  # check for playbook directory in current path
  if [ ! -d playbook ]; then
    echo "No playbook directory found"
    exit 1
  fi
  VERSION=$1   
  ENV_HOSTS="$2.ansible.hosts"
  PLAYBOOK="$3.yml"
fi

CMD_LINE="ansible-playbook -D -u ubuntu -e \"version=$VERSION\" -i $PLAYBOOK_DIR/$ENV_HOSTS $PLAYBOOK_DIR/$PLAYBOOK"

echo -e $CMD_LINE
exec $CMD_LINE


