#!/bin/sh

if [ -z "$1" ]; then
  echo "Error: Repo name parameter is missing."
  exit 1
fi

repo="$1"

# Ensure the logs directory exists
mkdir -p logs

# Open the log file for writing
LOGFILE="logs/gitolite_admin_read_branches_script.log"
exec >"$LOGFILE" 2>&1

# echo "Reading repo branches: $repo"

if [ ! -d branches ]; then
  mkdir branches
fi

cd branches || exit

GIT_SSH_COMMAND="ssh -p 22 -i gitolite -o StrictHostKeyChecking=no" git clone git@gitolite:"$repo"
if [ $? -ne 0 ]; then
  echo "Error: Cloning failed. Exiting."
  exit 1
fi

cd "$repo" || exit

GIT_SSH_COMMAND="ssh -p 22 -i ../../gitolite -o StrictHostKeyChecking=no" git for-each-ref --sort='-committerdate:iso8601' --format='%(committerdate:iso8601)|%(refname:short)|%(committername)' refs/remotes/ | sed 's/origin\///' | grep -v 'HEAD'

cd ..
rm -rf "$repo"
