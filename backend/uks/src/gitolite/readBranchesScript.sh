#!/bin/bash

if [ -z "$1" ]; then
  echo "Error: Repo name parameter is missing."
  exit 1
fi

repo="$1"

exec > >(tee -i logs/gitolite_admin_read_branches_script.log)
exec 2>&1

# echo "Reading repo branches: $repo"

cd gitolite-admin || exit 1

#GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git ls-remote git@localhost:"$repo".git | grep 'refs/heads/'

#GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git for-each-ref --sort='-committerdate:iso8601' --format='%(committerdate:relative)|%(refname:short)|%(committername)' refs/remotes/ | column -s '|' -t

GIT_SSH_COMMAND="ssh -p 2222 -i ../gitolite" git for-each-ref --sort='-committerdate:iso8601' --format='%(committerdate:iso8601)|%(refname:short)|%(committername)' refs/remotes/ | sed 's/origin\///' | grep -v 'HEAD' | column -s '|' -t