#!/bin/bash

if [ -z "$1" ]; then
  echo "Error: Repo name parameter is missing."
  exit 1
fi

repo="$1"

exec > >(tee -i logs/gitolite_admin_read_branches_script.log)
exec 2>&1

# echo "Reading repo branches: $repo"

if [ ! -d branches ]; then
  mkdir branches
fi

cd branches || exit

GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git clone git@localhost:"$repo"
if [ $? -ne 0 ]; then
  echo "Error: Cloning failed. Exiting."
  exit 1
fi

cd "$repo" || exit

GIT_SSH_COMMAND="ssh -p 2222 -i ../../gitolite" git for-each-ref --sort='-committerdate:iso8601' --format='%(committerdate:iso8601)|%(refname:short)|%(committername)' refs/remotes/ | sed 's/origin\///' | grep -v 'HEAD'

cd ..
rm -rf "$repo"
