#!/bin/bash

if [ -z "$1" ]; then
  echo "Error: Repo name parameter is missing."
  exit 1
fi

repo="$1"

exec > >(tee -i gitolite_admin_read_branches_script.log)
exec 2>&1

# echo "Reading repo branches: $repo"

cd gitolite-admin

GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git ls-remote git@localhost:"$repo".git | grep 'refs/heads/'
